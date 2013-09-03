package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class Parser {
	public static ArrayList<Long> getWayReferences(ArrayList<String> lines) {
		ArrayList<Long> out = new ArrayList<Long>();
		for (String line : lines) {
			String tag = Parser.getTag(line);
			if (tag.equals("member") && "way".equals(getValue(line, "type"))) {
				out.add(Long.parseLong(getValue(line, "ref")));
			}
		}
		return out;
	}
	protected static String getTag(String s) {
		int i = s.indexOf("<") + 1, j = s.indexOf(" ", i);
		if (j == -1) j = s.indexOf(">", i);
		return i >= 0 && i < j ? s.substring(i, j) : null;
	}
	protected static String getValue(String s, String key, String quote) {
		key += "=" + quote;
		int i = s.indexOf(key);
		if (i == -1)
			return null;
		int j = s.indexOf(quote, i + key.length());
		return s.substring(i + key.length(), j);
	}
	protected static String getValue(String s, String key) {
		String value = getValue(s, key, "\"");
		if (value == null) {
			return getValue(s, key, "'");
		} else {
			return value;
		}
	}
	protected static ArrayList<String> getLines(File f) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
			String s;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while ((s = reader.readLine()) != null) {
				lines.add(s);
			}
			reader.close();
		return lines;
	}
	protected static HashMap<Long, Point> getLocations(ArrayList<String> lines) {
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
	protected static void confirmDownloaded(ArrayList<String> lines, File f) throws IOException, FileNotYetAvailableException {
		if (lines.size() == 0)
			throw new FileNotYetAvailableException();

		String lastLine = lines.get(lines.size() - 1);
		if (!lastLine.trim().equals("</osm>")) {
			System.out.println(f);
			throw new FileNotYetAvailableException();
		}
	}


}
