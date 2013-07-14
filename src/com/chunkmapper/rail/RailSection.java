package com.chunkmapper.rail;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class RailSection {
	public final ArrayList<Point> points;
	public final boolean isPreserved, hasBridge, hasCutting, hasEmbankment, hasTunnel;
	public final Rectangle bbox;
//	public RailSection(ArrayList<Point> points,
//			boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel) {
//		this.isPreserved = isPreserved;
//		this.hasBridge = hasBridge;
//		this.hasCutting = hasCutting;
//		this.hasEmbankment = hasEmbankment;
//		this.hasTunnel = hasTunnel;
//		this.points = points;
//		bbox = null;
//	}
	public RailSection(ArrayList<Point> points,
			boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel,
			Rectangle bbox) {
		this.isPreserved = isPreserved;
		this.hasBridge = hasBridge;
		this.hasCutting = hasCutting;
		this.hasEmbankment = hasEmbankment;
		this.hasTunnel = hasTunnel;
		this.points = points;
		this.bbox = bbox;
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
		if (!(other instanceof RailSection))
			return false;
		RailSection other2 = (RailSection) other;
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}

}
