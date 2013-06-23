package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class RiverParser extends Parser {
	public static class RiverSection {
		public final ArrayList<Point> points;
		public final Rectangle bbox;
		public RiverSection(ArrayList<Point> points) {
			this.points = points;
			bbox = null;
		}
		public RiverSection(ArrayList<Point> points2, Rectangle bbox) {
			points = points2;
			this.bbox = bbox;
		}
	}
	public static Collection<RiverSection> getRiverSections(File f) throws IOException, FileNotYetAvailableException {
		ArrayList<String> lines = getLines(f);
		confirmDownloaded(lines, f);
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
