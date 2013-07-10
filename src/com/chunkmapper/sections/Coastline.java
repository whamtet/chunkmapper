package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class Coastline {
	public final ArrayList<Point> points;
	public final Rectangle bbox;
	public Coastline(ArrayList<Point> points, Rectangle bbox) {
		this.points = points;
//		this.points = new ArrayList<Point>();
//		Point previousPoint = points.get(0);
//		this.points.add(previousPoint);
//		for (Point point : points) {
//			if (!point.equals(previousPoint)) {
//				previousPoint = point;
//				this.points.add(point);
//			}
//		}
		this.bbox = bbox;
	}
//	public boolean contains(Coastline other) {
//		ArrayList<Point> op = other.points;
//		outer: for (int i = 0; i <= points.size() - op.size(); i++) {
//			for (int j = 0; j < op.size(); j++) {
//				if (!op.get(j).equals(points.get(i+j))) {
//					continue outer;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
	
	public int hashCode() {
		return bbox.hashCode();
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Coastline))
			return false;
		Coastline other2 = (Coastline) other;
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}

//	public Point getEndPoint() {
//		return points.get(points.size() - 1);
//	}
//	public boolean append(Coastline other) {
//		if (points.get(points.size() - 1).equals(other.points.get(0))) {
//			for (int i = 1; i < other.points.size(); i++) {
//				points.add(other.points.get(i));
//			}
//			return true;
//		}
//		return false;
//	}
//	public boolean isClosed() {
//		return points.get(0).equals(points.get(points.size() - 1));
//	}
//	public boolean isOpen() {
//		return !isClosed();
//	}
//	public boolean isSinglePoint() {
//		Point first = points.get(0);
//		for (Point other : points) {
//			if (!other.equals(first)) {
//				return false;
//			}
//		}
//		return true;
//	}
}
