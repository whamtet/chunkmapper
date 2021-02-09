package com.chunkmapper.parser;

import java.util.ArrayList;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.HighwaySection;

public class HighwayParser extends Parser {

	public static ArrayList<HighwaySection> getHighwaySections(OverpassObject o) {
		ArrayList<HighwaySection> out = new ArrayList<HighwaySection>();
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
