package com.chunkmapper.parser;

import java.awt.Rectangle;
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

public class OverpassParser extends Parser {
	private static final ConcurrentHashMap<Point, OverpassObject> cache = new ConcurrentHashMap<Point, OverpassObject>();
	//yoo hoo
	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("taupo");
		//		double[] latlon = Nominatim.getPoint("te anau, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		getObject(regionx, regionz);
	}
	private static ArrayList<String> getLines() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("test.xml"));
		ArrayList<String> out = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			out.add(line);
		}
		reader.close();
		return out;
	}
	public static class Relation {
		public final ArrayList<Way> ways = new ArrayList<Way>();
		public final HashMap<String, String> map = new HashMap<String, String>();
		public Rectangle bbox;
		public void calculateBbox() {
			int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;
			int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
			for (Way way : ways) {
				if (way.bbox.x < minx)
					minx = way.bbox.x;
				if (way.bbox.y < minz)
					minz = way.bbox.y;
				int x2 = way.bbox.x + way.bbox.width;
				if (x2 > maxx)
					maxx = x2;
				int z2 = way.bbox.y + way.bbox.height;
				if (z2 > maxz)
					maxz = z2;
			}
			bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
		}
	}
	public static class Way {
		public final ArrayList<Point> points = new ArrayList<Point>();
		public final HashMap<String, String> map = new HashMap<String, String>();
		public Rectangle bbox;
		public String toString() {
			return points.toString() + "\n" + map.toString();
		}
		public void calculateBbox() {
			int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;
			int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
			for (Point p : points) {
				if (p.x > maxx)
					maxx = p.x;
				if (p.z > maxz)
					maxz = p.z;
				if (p.x < minx)
					minx = p.x;
				if (p.z < minz)
					minz = p.z;
			}
			bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
		}
	}
	public static class Node {
		public final Point point;
		public final HashMap<String, String> map = new HashMap<String, String>();
		public Node(Point p) {
			point = p;
		}
		public String toString() {
			return point.toString() + "\n" + map.toString();
		}
	}
	public static class OverpassObject {
		public final ArrayList<Way> ways;
		public final ArrayList<Node> nodes;
		public final ArrayList<Relation> relations;
		public OverpassObject(ArrayList<Way> ways, ArrayList<Node> nodes, ArrayList<Relation> relations) {
			this.ways = ways;
			this.nodes = nodes;
			this.relations = relations;
		}
	}
	public static OverpassObject getObject(int regionx, int regionz) throws IOException {
		Point p = new Point(regionx, regionz);
		if (cache.contains(p)) {
			return cache.get(p);
		} else {
			OverpassObject o = doGetObject(regionx, regionz);
			cache.put(p, o);
			return o;
		}
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

	private static OverpassObject doGetObject(int regionx, int regionz) throws IOException {
		ArrayList<String> lines = OverpassDownloader.getLines(regionx, regionz);
//				spit(lines);
		//		ArrayList<String> lines = getLines();
		HashMap<Long, Point> locations = getLocations(lines);
		ArrayList<Way> ways = new ArrayList<Way>();
		HashMap<Long, Way> wayMap = new HashMap<Long, Way>();
		long wayKey = 0;
		Way currWay = null;
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node currNode = null;

		for (String line : lines) {
			String tag = getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currWay = new Way();
				wayKey = Long.parseLong(getValue(line, "id"));
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
				ways.add(currWay);
				wayMap.put(wayKey, currWay);
				currWay = null;
			}
			if (tag.equals("node") && line.indexOf("/>") == -1) {
				Long k = Long.parseLong(getValue(line, "id"));
				currNode = new Node(locations.get(k));
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
				nodes.add(currNode);
			}
		}
		Relation relation = null;
		ArrayList<Relation> relations = new ArrayList<Relation>();
		for (String line : lines) {
			String tag = getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("relation")) {
				relation = new Relation();
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
				relations.add(relation);
				relation = null;
			}
		}

		return new OverpassObject(ways, nodes, relations);
	}

}
