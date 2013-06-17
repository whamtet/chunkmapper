package com.chunkmapper.rail;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class RailSection {
	public final ArrayList<Point> points;
	public final boolean isPreserved, hasBridge, hasCutting, hasEmbankment, hasTunnel;
	public RailSection(ArrayList<Point> points,
			boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel) {
		this.isPreserved = isPreserved;
		this.hasBridge = hasBridge;
		this.hasCutting = hasCutting;
		this.hasEmbankment = hasEmbankment;
		this.hasTunnel = hasTunnel;
		this.points = points;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("isPreserved " + isPreserved + "\n");
		builder.append("hasBridge " + hasBridge + "\n");
		builder.append("hasCutting " + hasCutting + "\n");
		builder.append("hasEmbankment " + hasEmbankment + "\n");
		builder.append("hasTunnel " + hasTunnel + "\n");
		for (Point p : points) {
			builder.append(p.x + " " + p.z + "\n");
		}
		builder.append("***\n");
		return builder.toString();
	}
	private static boolean getBoolean(BufferedReader in) throws IOException {
		return Boolean.parseBoolean(in.readLine().split(" ")[1]);
	}
	private static Point getPoint(String s) throws IOException {
		String[] points = s.split(" ");
		int x = Integer.parseInt(points[0]);
		int z = Integer.parseInt(points[1]);
		return new Point(x, z);
	}
	
	public RailSection(BufferedReader in) throws IOException {
		this.isPreserved = getBoolean(in);
		this.hasBridge = getBoolean(in);
		this.hasCutting = getBoolean(in);
		this.hasEmbankment = getBoolean(in);
		this.hasTunnel = getBoolean(in);
		
		this.points = new ArrayList<Point>();
		String line;
		while (!(line = in.readLine()).equals("***\n")) {
			points.add(getPoint(line));
		}
	}
	public boolean intersectsRegion(int regionx, int regionz) {
		Rectangle region = new Rectangle(regionx * 512, regionz * 512, 512, 512);
		for (Point p : points) {
			if (region.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
	}
}
