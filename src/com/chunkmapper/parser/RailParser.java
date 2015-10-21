package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.binaryparser.OSMRouter;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.reader.DensityReader;
import com.chunkmapper.sections.POI;
import com.chunkmapper.sections.RailSection;

public class RailParser extends Parser {
	
	public static void main(String[] args) throws Exception {
		BucketInfo.initMap();
		double[] latlon = Nominatim.getPoint("New York");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		OverpassObject o = OSMRouter.getObject(regionx, regionz);
		HashSet<String> vals = new HashSet<String>();
		for (Way way : o.ways) {
			if (way.map.containsKey("railway")) {
				vals.add(way.map.get("railway"));
			}
		}
		System.out.println(vals);
		System.out.println("done");
	}
	public static ArrayList<RailSection> getRailSection(DensityReader densityReader, OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException, URISyntaxException {
		ArrayList<RailSection> out = new ArrayList<RailSection>();
		for (Way way : o.ways) {
			String railway = way.map.get("railway");
			Point rootPoint = way.points.size() > 0 ? way.points.get(0) : null;
			if (railway != null && (railway.contains("rail") ||
					densityReader != null && rootPoint != null && !densityReader.isUrbanxz(rootPoint.x, rootPoint.z)
					&& (railway.equals("abandoned") || railway.equals("disused")))) {
//			if (railway != null && (railway.contains("rail") || railway.equals("abandoned") || railway.equals("disused"))
//					&& rootPoint != null && densityReader.isUrbanxz(rootPoint.x, rootPoint.z)
//					) {
				
				boolean isPreserved = "preserved".equals(way.map.get("railway"));
				boolean hasBridge = "yes".equals(way.map.get("bridge"));
				boolean hasCutting = "yes".equals(way.map.get("cutting"));
				boolean embankment = "yes".equals(way.map.get("embankment"));
				boolean hasTunnel = "yes".equals(way.map.get("tunnel"));
				out.add(new RailSection(way.points, isPreserved, hasBridge, hasCutting, embankment, hasTunnel, way.bbox));
			}
		}
		return out;
	}
	
	public static ArrayList<RailSection> getRailSections(ArrayList<String> lines) {
		
		HashMap<Long, Point> locations = RailParser.getLocations(lines);
		ArrayList<RailSection> railSections = new ArrayList<RailSection>();
		ArrayList<Point> currentPoints = null;
		boolean isRail = false;
		boolean isPreserved = false;
		boolean hasBridge = false;
		boolean hasCutting = false;
		boolean hasEmbankment = false;
		boolean hasTunnel = false;
		int minx = 0, maxx = 0, minz = 0, maxz = 0;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isRail = false;
				isPreserved = false;
				hasBridge = false;
				hasCutting = false;
				hasEmbankment= false;
				hasTunnel = false;
				minx = Integer.MAX_VALUE; minz = Integer.MAX_VALUE;
				maxx = Integer.MIN_VALUE; maxz = Integer.MIN_VALUE;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(RailParser.getValue(line, "ref"));
				Point p = locations.get(ref);
				if (p.x < minx)
					minx = p.x;
				if (p.z < minz) 
					minz = p.z;
				if (p.x > maxx)
					maxx = p.x;
				if (p.z > maxz)
					maxz = p.z;
				currentPoints.add(locations.get(ref));
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isRail |= k.equals("railway");
				isPreserved |= k.equals("railway") && v.equals("preserved");
				
				hasBridge |= k.equals("bridge") && v.equals("yes");
				hasCutting |= k.equals("cutting") && v.equals("yes");
				hasEmbankment |= k.equals("embankment") && v.equals("yes");
				hasTunnel |= k.equals("tunnel") && v.equals("yes");
			}
			if (tag.equals("/way") && (isRail || isPreserved)) {
				Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
				railSections.add(new RailSection(currentPoints, 
						isPreserved, hasBridge, hasCutting, hasEmbankment, hasTunnel, bbox));
			}
		}
		return railSections;
	}
}
