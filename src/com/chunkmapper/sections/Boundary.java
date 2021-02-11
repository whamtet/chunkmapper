package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class Boundary extends Section implements Comparable<Boundary> {
	public final ArrayList<Point> points;
	public final Rectangle bbox;
	public final String leftArea, rightArea;
	public final int adminLevel;
	public Boundary(ArrayList<Point> points, Rectangle bbox, String leftCountry, String rightCountry, int adminLevel) {
		this.points = points;
		this.bbox = bbox;
		this.leftArea = leftCountry;
		this.rightArea = rightCountry;
		this.adminLevel = adminLevel;
	}

	public int hashCode() {
		return bbox.hashCode();
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Boundary))
			return false;
		Boundary other2 = (Boundary) other;
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}
	@Override
	public int compareTo(Boundary o) {
		if (points.size() != o.points.size() || points.size() == 0) {
			return (new Integer(points.size())).compareTo(o.points.size());
		}
		return points.get(0).compareTo(o.points.get(0));
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Point p : points) {
			sb.append(p.x + ", " + p.z + "; ");
		}
		return sb.toString();
	}

}
