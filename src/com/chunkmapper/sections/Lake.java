package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.Point;

public class Lake {
	public ArrayList<Point> points;
	public final Rectangle bbox;
	public final boolean isCove, isLagoon;
	
	public Lake(ArrayList<Point> points, Rectangle bbox, boolean isCove, boolean isLagoon) {
		this.points = points;
		this.bbox = bbox;
		this.isCove = isCove;
		this.isLagoon = isLagoon;
		
	}
	public boolean isPoint() {
		return points.size() < 2;
	}
	public boolean contains(Lake other) {
		ArrayList<Point> op = other.points;
		outer: for (int i = 0; i <= points.size() - op.size(); i++) {
			for (int j = 0; j < op.size(); j++) {
				if (!op.get(j).equals(points.get(i+j))) {
					continue outer;
				}
			}
			return true;
		}
		return false;
	}
	
	public int hashCode() {
		return bbox.hashCode();
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Lake))
			return false;
		Lake other2 = (Lake) other;
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}
	public boolean isClosed() {
		return points.get(0).equals(points.get(points.size() - 1));
	}
	public boolean isOpen() {
		return !isClosed();
	}
	public Point getEndPoint() {
		return points.get(points.size() - 1);
	}
	public boolean connect(Lake other) {
		if (points.get(points.size() - 1).equals(other.points.get(0))) {
			for (int i = 1; i < other.points.size(); i++) {
				points.add(other.points.get(i));
			}
			return true;
		}
		return false;
	}
	

	

}
