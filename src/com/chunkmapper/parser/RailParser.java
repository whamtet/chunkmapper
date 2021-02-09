package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.reader.DensityReader;
import com.chunkmapper.sections.RailSection;

public class RailParser extends Parser {

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
}
