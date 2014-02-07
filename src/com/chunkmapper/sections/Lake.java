package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class Lake extends Section {
	public ArrayList<Point> points;
	public final Rectangle bbox;
	public final boolean isCove, isLagoon;
	
	public Lake(ArrayList<Point> points, Rectangle bbox) {
		this.points = points;
		this.bbox = bbox;
		isCove = false;
		isLagoon = false;
	}
	
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
		return points.size() > 0 && points.get(0).equals(points.get(points.size() - 1));
	}
	public boolean isOpen() {
		return !isClosed();
	}
	public Point getEndPoint() {
		return points.get(points.size() - 1);
	}
	public boolean attach(Lake other) {
		Point endPoint = points.get(points.size() - 1), otherStartPoint = other.points.get(0);
		if (endPoint.equals(otherStartPoint)) {
			for (int i = 1; i < other.points.size(); i++) {
				points.add(other.points.get(i));
			}
			return true;
		}
		return false;
	}
	public boolean attachEitherEnd(Lake other) {
		
		Point startPoint = points.get(0), endPoint = points.get(points.size() - 1);
		Point otherStartPoint = other.points.get(0), otherEndPoint = other.points.get(other.points.size() - 1);
		
		if (startPoint.equals(otherStartPoint)) {
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for (int i = other.points.size() - 1; i > 0; i--) {
				newPoints.add(other.points.get(i));
			}
			for (Point p : points) {
				newPoints.add(p);
			}
			points = newPoints;
			return true;
		}
		if (startPoint.equals(otherEndPoint)) {
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for (int i = 0; i < other.points.size() - 1; i++) {
				newPoints.add(other.points.get(i));
			}
			for (Point p : points) {
				newPoints.add(p);
			}
			points = newPoints;
			return true;
		}
		if (endPoint.equals(otherStartPoint)) {
			for (int i = 1; i < other.points.size(); i++) {
				points.add(other.points.get(i));
			}
			return true;
		}
		if (endPoint.equals(otherEndPoint)) {
			for (int i = other.points.size() - 2; i >= 0; i--) {
				points.add(other.points.get(i));
			}
			return true;
		}
		return false;
		
	}
	public static void main(String[] args) {
		ArrayList<Point> a1 = new ArrayList<Point>(), a2 = new ArrayList<Point>(), a3 = new ArrayList<Point>(), a4 = new ArrayList<Point>();
		a1.add(new Point(0, 0));
		a1.add(new Point(1, 0));
		Lake l1 = new Lake(a1, null);
		a2.add(new Point(2, 0));
		a2.add(new Point(1, 0));
		Lake l2 = new Lake(a2, null);
		l1.attachEitherEnd(l2);
		System.out.println(l1.points);
	}
//	public boolean connect(Lake other) {
//		if (points.get(points.size() - 1).equals(other.points.get(0))) {
//			for (int i = 1; i < other.points.size(); i++) {
//				points.add(other.points.get(i));
//			}
//			return true;
//		}
//		return false;
//	}
	

	

}
