package com.chunkmapper.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.OverpassObjectSource;
import com.chunkmapper.downloader.OverpassDownloader;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.parser.OverpassObject.Relation;
import com.chunkmapper.parser.OverpassObject.Way;

public class OverpassParser extends Parser implements OverpassObjectSource {
	public OverpassObject getObject(int regionx, int regionz) throws IOException {
		return doGetObject(regionx, regionz, false);
	}
	public static OverpassObject getTestObject(int regionx, int regionz) throws IOException {
		return doGetObject(regionx, regionz, true);
	}

	private static OverpassObject doGetObject(int regionx, int regionz, boolean test) throws IOException {
		ArrayList<String> lines = OverpassDownloader.getLines(regionx, regionz, test);

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
					throw new RuntimeException("Invalid Overpass xml");
				}
				//				boolean include = false;
				//				for (String k : currNode.map.keySet()) {
				//					String v = currNode.map.get(k);
				//					include |= include(k, v, include);
				//				}
				//				if (include)
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
