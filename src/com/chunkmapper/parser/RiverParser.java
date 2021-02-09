package com.chunkmapper.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.OSMRouter;
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
