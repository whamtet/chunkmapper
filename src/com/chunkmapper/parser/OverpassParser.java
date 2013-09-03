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
//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("christchurch, nz");
//		//		double[] latlon = Nominatim.getPoint("te anau, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		getObjects(regionx, regionz);
//	}
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
		public OverpassObject(ArrayList<Way> ways, ArrayList<Node> nodes) {
			this.ways = ways;
			this.nodes = nodes;
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
	}
	
	private static OverpassObject doGetObject(int regionx, int regionz) throws IOException {
		ArrayList<String> lines = OverpassDownloader.getLines(regionx, regionz);
		spit(lines);
//		ArrayList<String> lines = getLines();
		HashMap<Long, Point> locations = getLocations(lines);
		ArrayList<Way> ways = new ArrayList<Way>();
		Way currWay = null;
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node currNode = null;
		
		for (String line : lines) {
			String tag = getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currWay = new Way();
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
		
		return new OverpassObject(ways, nodes);
	}

}
