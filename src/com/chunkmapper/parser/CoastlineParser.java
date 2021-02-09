package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
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
