package com.chunkmapper.rail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class MyParser {
	private static String getTag(String s) {
		int i = s.indexOf("<") + 1, j = s.indexOf(" ", i);
		if (j == -1) j = s.indexOf(">", i);
		return i >= 0 && i < j ? s.substring(i, j) : null;
	}
	private static String getValue(String s, String key) {
		key += "=\"";
		int i = s.indexOf(key);
		if (i == -1)
			return null;
		int j = s.indexOf("\"", i + key.length());
		return s.substring(i + key.length(), j);
	}
	private static ArrayList<String> getLines(File f) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
			String s;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while ((s = reader.readLine()) != null) {
				lines.add(s);
			}
			reader.close();
		return lines;
	}
	private static HashMap<Long, Point> getLocations(ArrayList<String> lines) {
		HashMap<Long, Point> locations = new HashMap<Long, Point>();
		for (String line : lines) {
			String tag = getTag(line);
			if (tag != null && tag.equals("node")) {
				long id = Long.parseLong(getValue(line, "id"));
				double lat = Double.parseDouble(getValue(line, "lat"));
				double lon = Double.parseDouble(getValue(line, "lon"));
				int x = (int) (lon * 3600), z = (int) (-lat * 3600);
				locations.put(id, new Point(x, z));
			}
		}
		return locations;
	}
	private static void confirmDownloaded(ArrayList<String> lines, File f) throws IOException, FileNotYetAvailableException {
		if (lines.size() == 0)
			throw new FileNotYetAvailableException();

		String lastLine = lines.get(lines.size() - 1);
		if (!lastLine.trim().equals("</osm>")) {
			System.out.println(f);
			throw new FileNotYetAvailableException();
		}
	}

	public static ArrayList<RailSection> getRailSections(File f) throws IOException, FileNotYetAvailableException {
		ArrayList<String> lines = MyParser.getLines(f);
		confirmDownloaded(lines, f);
		HashMap<Long, Point> locations = MyParser.getLocations(lines);
		ArrayList<RailSection> railSections = new ArrayList<RailSection>();
		ArrayList<Point> currentPoints = null;
		boolean addThisSection = false;
		boolean hasBridge = false;
		boolean hasCutting = false;
		boolean hasEmbankment = false;
		boolean hasTunnel = false;
		for (String line : lines) {
			String tag = MyParser.getTag(line);
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
				long ref = Long.parseLong(MyParser.getValue(line, "ref"));
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
