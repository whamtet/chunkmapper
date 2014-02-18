package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.chunkmapper.Point;
import com.chunkmapper.downloader.OverpassDownloader;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.parser.OverpassObject.Relation;
import com.chunkmapper.parser.OverpassObject.Way;

public class OverpassParser extends Parser {
//	private static ConcurrentHashMap<Point, OverpassObject> cache = new ConcurrentHashMap<Point, OverpassObject>();

//	private static ArrayList<String> getLines() throws IOException {
//		
//		BufferedReader reader = new BufferedReader(new FileReader("test.xml"));
//		ArrayList<String> out = new ArrayList<String>();
//		String line;
//		while ((line = reader.readLine()) != null) {
//			out.add(line);
//		}
//		reader.close();
//		return out;
//	}
	
//	public static void flushCache() {
//		cache = new ConcurrentHashMap<Point, OverpassObject>();
//	}
	public static OverpassObject getObject(int regionx, int regionz) throws IOException {
		//		if (true)
		//		throw new RuntimeException("Don't call me");
//		Point p = new Point(regionx, regionz);
//		if (cache.containsKey(p)) {
//			return cache.get(p);
//		} else {
//			OverpassObject o = doGetObject(regionx, regionz, false);
//			cache.put(p, o);
//			return o;
//		}
		return doGetObject(regionx, regionz, false);
	}
	public static OverpassObject getTestObject(int regionx, int regionz) throws IOException {
		return doGetObject(regionx, regionz, true);
	}
	private static void spit(ArrayList<String> lines) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("test.xml")));
		for (String line : lines) {
			pw.println(line);
		}
		pw.close();
		Runtime.getRuntime().exec("open test.xml");
		//		System.exit(0);
	}

	private static OverpassObject doGetObject(int regionx, int regionz, boolean test) throws IOException {
		ArrayList<String> lines = OverpassDownloader.getLines(regionx, regionz, test);
		//				spit(lines);
		//		ArrayList<String> lines = getLines();
		HashMap<Long, Point> locations = getLocations(lines);
		OverpassObject out = new OverpassObject();
		//		ArrayList<Way> ways = new ArrayList<Way>();
		HashMap<Long, Way> wayMap = new HashMap<Long, Way>();
		long wayKey = 0;
		Way currWay = null;
		//		ArrayList<Node> nodes = new ArrayList<Node>();
		Node currNode = null;

		for (String line : lines) {
			String tag = getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {

				wayKey = Long.parseLong(getValue(line, "id"));
				currWay = new Way(wayKey);
			}
			if (tag.equals("nd")) {
				if (currWay != null) {
					Long k = Long.parseLong(getValue(line, "ref"));
					currWay.points.add(locations.get(k));
				}
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				if (currWay != null) {
					currWay.map.put(k, v);
				}
				if (currNode != null) {
					currNode.map.put(k, v);
				}
			}
			if (tag.equals("/way")) {
				currWay.calculateBbox();
				//				ways.add(currWay);
				out.ways.add(currWay);
				wayMap.put(wayKey, currWay);
				currWay = null;
			}
			if (tag.equals("node") && line.indexOf("/>") == -1) {
				Long k = Long.parseLong(getValue(line, "id"));
				currNode = new Node(locations.get(k), k);
			}
			if (tag.equals("/node")) {
				if (currNode == null) {
					PrintWriter pw = new PrintWriter("error.xml");
					for (String line2 : lines) {
						pw.println(line2);
					}
					pw.close();
					Runtime.getRuntime().exec("open error.xml");
					System.exit(0);
				}
				boolean include = false;
				for (String k : currNode.map.keySet()) {
					String v = currNode.map.get(k);
					include |= include(k, v, include);
				}
				if (include)
					out.nodes.add(currNode);
			}
		}
		Relation relation = null;
		//		ArrayList<Relation> relations = new ArrayList<Relation>();
		for (String line : lines) {
			String tag = getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("relation")) {
				Long id = Long.parseLong(getValue(line, "id"));
				relation = new Relation(id);
			}
			if (tag.equals("member")) {
				long key = Long.parseLong(getValue(line, "ref"));
				Way way = wayMap.get(key);
				if (way != null) {
					way.map.put("role", getValue(line, "role"));
					relation.ways.add(way);
				}
			}
			if (tag.equals("tag")) {
				if (relation != null) {
					String k = getValue(line, "k"), v = getValue(line, "v");
					relation.map.put(k, v);
				}
			}
			if (tag.equals("/relation")) {
				relation.calculateBbox();
				//				relations.add(relation);
				out.relations.add(relation);
				relation = null;
			}
		}

		return out;
	}
	public static boolean include(String k, String v, boolean include) {
		include |= k.equals("place");
		include |= k.equals("tourism") && (v.equals("alpine_hut") || v.equals("wilderness_hut"));
		include |= k.equals("sport") && (v.equals("rugby") || v.equals("rugby_league") || v.equals("rugby_union"));
		return include;
	}

}
