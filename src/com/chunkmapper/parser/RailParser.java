package com.chunkmapper.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.rail.RailSection;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class RailParser extends Parser {
	
	public static ArrayList<RailSection> getRailSections(File f) throws IOException, FileNotYetAvailableException {
		ArrayList<String> lines = RailParser.getLines(f);
		confirmDownloaded(lines, f);
		HashMap<Long, Point> locations = RailParser.getLocations(lines);
		ArrayList<RailSection> railSections = new ArrayList<RailSection>();
		ArrayList<Point> currentPoints = null;
		boolean isRail = false;
		boolean isPreserved = false;
		boolean hasBridge = false;
		boolean hasCutting = false;
		boolean hasEmbankment = false;
		boolean hasTunnel = false;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isRail = false;
				isPreserved = false;
				hasBridge = false;
				hasCutting = false;
				hasEmbankment= false;
				hasTunnel = false;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(RailParser.getValue(line, "ref"));
				currentPoints.add(locations.get(ref));
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isRail |= k.equals("railway") && v.equals("rail");
				isPreserved |= k.equals("railway") && v.equals("preserved");
				
				hasBridge |= k.equals("bridge") && v.equals("yes");
				hasCutting |= k.equals("cutting") && v.equals("yes");
				hasEmbankment |= k.equals("embankment") && v.equals("yes");
				hasTunnel |= k.equals("tunnel") && v.equals("yes");
			}
			if (tag.equals("/way") && (isRail || isPreserved)) {
				boolean allowAscend = !hasTunnel && !hasCutting;
				boolean allowDescend = !hasBridge && !hasEmbankment;
				railSections.add(new RailSection(currentPoints, allowAscend, allowDescend, isPreserved));
			}
		}
		return railSections;
	}
}
