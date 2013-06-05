package com.chunkmapper.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.rail.RailSection;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class RiverParser extends Parser {
	public static class RiverSection {
		public final ArrayList<Point> points;
		public RiverSection(ArrayList<Point> points) {
			this.points = points;
		}
	}
	public static Collection<RiverSection> getRiverSections(File f) throws IOException, FileNotYetAvailableException {
		ArrayList<String> lines = RailParser.getLines(f);
		confirmDownloaded(lines, f);
		HashMap<Long, Point> locations = RailParser.getLocations(lines);
		ArrayList<RiverSection> riverSections = new ArrayList<RiverSection>();
//		HashMap<String, RiverSection> riverSections = new HashMap<String, RiverSection>();
		
		boolean isCorrectWay = false;
		String sectionName = null;
		ArrayList<Point> currentPoints = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isCorrectWay = false;
				sectionName = null;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(RailParser.getValue(line, "ref"));
				currentPoints.add(locations.get(ref));
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isCorrectWay |= k.equals("waterway") && v.equals("river");
				if (k.equals("name"))
					sectionName = v;
				
			}
			if (tag.equals("/way") && isCorrectWay && sectionName != null) {
				riverSections.add(new RiverSection(currentPoints));
//				RiverSection toAddTo = riverSections.get(sectionName);
//				if (toAddTo == null) {
//					riverSections.put(sectionName, new RiverSection(currentPoints));
//				} else {
//					for (Point p : currentPoints) {
//						toAddTo.points.add(p);
//					}
//				}
			}
		}
//		return riverSections.values();
		return riverSections;
	}

}
