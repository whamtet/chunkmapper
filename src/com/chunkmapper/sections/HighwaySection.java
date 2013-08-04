package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class HighwaySection extends Section {
	public final ArrayList<Point> points;
	public final boolean hasBridge, hasTunnel;
	public final Rectangle bbox;
	public final String name;

	public HighwaySection(ArrayList<Point> points,
			boolean hasBridge, boolean hasTunnel,
			Rectangle bbox, String name) {
		this.hasBridge = hasBridge;
		this.hasTunnel = hasTunnel;
		this.points = points;
		this.bbox = bbox;
		this.name = name;
	}
	public int hashCode() {
		return bbox.hashCode();
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Point p : points) {
			sb.append(p.x + ", " + p.z + "; ");
		}
		return sb.toString();
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof HighwaySection))
			return false;
		HighwaySection other2 = (HighwaySection) other;
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}

}
