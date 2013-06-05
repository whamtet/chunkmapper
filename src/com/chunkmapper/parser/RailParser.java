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
		boolean addThisSection = false;
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
				addThisSection = false;
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
				addThisSection |= k.equals("railway") && v.equals("rail");
				
				hasBridge |= k.equals("bridge") && v.equals("yes");
				hasCutting |= k.equals("cutting") && v.equals("yes");
				hasEmbankment |= k.equals("embankment") && v.equals("yes");
				hasTunnel |= k.equals("tunnel") && v.equals("yes");
			}
			if (tag.equals("/way") && addThisSection) {
				boolean allowAscend = !hasTunnel && !hasCutting;
				boolean allowDescend = !hasBridge && !hasEmbankment;
				railSections.add(new RailSection(currentPoints, allowAscend, allowDescend));
			}
		}
		return railSections;
	}
	//	public static void main(String[] args) throws Exception {
	//		File f = new File("/Library/Caches/Chunkmapper/xapirail/f_-1_-361.xml");
	//		ArrayList<String> lines = MyParser.getLines(f);
	//		for (String line : lines) {
	//			String tag = MyParser.getTag(line);
	//			if (tag != null) {
	//				if (tag.endsWith("way"))
	//					System.out.println(tag);
	//			}
	//		}
	//	}
}
