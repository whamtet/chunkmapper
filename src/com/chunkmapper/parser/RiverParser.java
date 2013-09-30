package com.chunkmapper.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.RiverSection;

public class RiverParser extends Parser {
	
	public static ArrayList<RiverSection> getRiverSections(int regionx, int regionz) throws IOException {
		ArrayList<RiverSection> out = new ArrayList<RiverSection>();
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		for (Way way : o.ways) {
			if ("river".equals(way.map.get("waterway"))) {
				out.add(new RiverSection(way.points));
			}
		}
		return out;
	}
	
	public static Collection<RiverSection> getRiverSections(ArrayList<String> lines) {
		HashMap<Long, Point> locations = getLocations(lines);
		System.out.println(locations.size());
		ArrayList<RiverSection> riverSections = new ArrayList<RiverSection>();
//		HashMap<String, RiverSection> riverSections = new HashMap<String, RiverSection>();
		
		boolean isCorrectWay = false;
		ArrayList<Point> currentPoints = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isCorrectWay = false;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(RailParser.getValue(line, "ref"));
				currentPoints.add(locations.get(ref));
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isCorrectWay |= k.equals("waterway") && v.equals("river");
				
			}
			if (tag.equals("/way") && isCorrectWay ) {
				riverSections.add(new RiverSection(currentPoints));
			}
		}
		return riverSections;
	}

}
