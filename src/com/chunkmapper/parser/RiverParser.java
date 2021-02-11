package com.chunkmapper.parser;

import java.util.ArrayList;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.RiverSection;

public class RiverParser extends Parser {
	
	public static ArrayList<RiverSection> getRiverSections(OverpassObject o) {
		ArrayList<RiverSection> out = new ArrayList<RiverSection>();
		for (Way way : o.ways) {
			if ("river".equals(way.map.get("waterway"))) {
				out.add(new RiverSection(way.points));
			}
		}
		return out;
	}

}
