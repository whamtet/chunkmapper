package com.chunkmapper.parser;

import java.util.HashSet;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.Coastline;

public class CoastlineParser extends Parser {
	
	public static HashSet<Coastline> getCoastlines(OverpassObject o) {
		HashSet<Coastline> coastlines = new HashSet<Coastline>();
		for (Way way : o.ways) {
			if ("coastline".equals(way.map.get("natural"))) {
				coastlines.add(new Coastline(way.points, way.bbox));
			}
		}
		return coastlines;
	}
}
