package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.Point;

public  class OverpassObject {
	
	public static class Relation {
		public final ArrayList<Way> ways = new ArrayList<Way>();
		public final HashMap<String, String> map = new HashMap<String, String>();
		public Rectangle bbox;
		public final long id;
		public Relation(long id) {
			this.id = id;
		}
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
		public int hashCode() {
			return bbox.hashCode();
		}
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof Relation))
				return false;
			Relation other2 = (Relation) other;
			return other2.id == id;
		}
	}
	public static class Way {
		public final ArrayList<Point> points = new ArrayList<Point>();
		public final HashMap<String, String> map = new HashMap<String, String>();
		public Rectangle bbox;
		public final long id;
		public Way(long id) {
			this.id = id;
		}
		public String toString() {
			return points.toString() + "\n" + map.toString();
		}
		public int hashCode() {
			return bbox.hashCode();
		}
		public boolean equals(Object other) {
			if (other == null) 
				return false;
			if (!(other instanceof Way))
				return false;
			Way other2 = (Way) other;
			return other2.id == id;
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
		public final long id;
		
		public int hashCode() {
			return (int) id;
		}
		public boolean equals(Object other) {
			if (other == null) 
				return false;
			if (!(other instanceof Node))
				return false;
			Node other2 = (Node) other;
			return other2.id == id;
		}
		
		public Node(Point p, long id) {
			this.id = id;
			point = p;
		}
		public String toString() {
			return point.toString() + "\n" + map.toString();
		}
	}
//	public final ArrayList<Way> ways;
//	public final ArrayList<Node> nodes;
//	public final ArrayList<Relation> relations;
//	public OverpassObject() {
//		ways = new ArrayList<Way>();
//		nodes = new ArrayList<Node>();
//		relations = new ArrayList<Relation>();
//	}
	public final HashSet<Way> ways;
	public final HashSet<Node> nodes;
	public final HashSet<Relation> relations;
	public OverpassObject() {
		ways = new HashSet<Way>();
		nodes = new HashSet<Node>();
		relations = new HashSet<Relation>();
	}
//	public OverpassObject(ArrayList<Way> ways, ArrayList<Node> nodes, ArrayList<Relation> relations) {
//		this.ways = ways;
//		this.nodes = nodes;
//		this.relations = relations;
//	}
	
	public String toString() {
		return String.format("nodes: %s, ways: %s, relations: %s", nodes.size(), ways.size(), relations.size());
	}
}