package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.HighwaySection;

public class HighwayParser extends Parser {
	
	public static ArrayList<HighwaySection> getHighwaySections(ArrayList<String> lines) {
		
		HashMap<Long, Point> locations = HighwayParser.getLocations(lines);
		ArrayList<HighwaySection> highwaySections = new ArrayList<HighwaySection>();
		ArrayList<Point> currentPoints = null;
		boolean isHighway = false;
		boolean hasBridge = false;
		boolean hasTunnel = false;
		String name = null;
		int minx = 0, maxx = 0, minz = 0, maxz = 0;
		for (String line : lines) {
			String tag = HighwayParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isHighway = false;
				hasBridge = false;
				hasTunnel = false;
				name = null;
				minx = Integer.MAX_VALUE; minz = Integer.MAX_VALUE;
				maxx = Integer.MIN_VALUE; maxz = Integer.MIN_VALUE;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(HighwayParser.getValue(line, "ref"));
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
				isHighway |= k.equals("highway");
				if (k.equals("name"))
					name = v;
				hasBridge |= k.equals("bridge") && v.equals("yes");
				hasTunnel |= k.equals("tunnel") && v.equals("yes");
			}
			if (tag.equals("/way") && isHighway) {
				Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
				highwaySections.add(new HighwaySection(currentPoints, hasBridge, hasTunnel, bbox, name));
			}
		}
		return highwaySections;
	}
	public static ArrayList<HighwaySection> getHighwaySections(int regionx, int regionz) throws IOException {
		ArrayList<HighwaySection> out = new ArrayList<HighwaySection>();
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		for (Way way : o.ways) {
			String k = way.map.get("highway");
			if ("motorway".equals(k) || "trunk".equals(k) || "primary".equals(k)) {
				String name = way.map.get("name");
				boolean hasBridge = "yes".equals(way.map.get("bridge"));
				boolean hasTunnel = "yes".equals(way.map.get("tunnel"));
				out.add(new HighwaySection(way.points, hasBridge, hasTunnel, way.bbox, name));
			}
		}
		return out;
	}
}
