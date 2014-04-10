package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;

public class Glacier extends Section {
	public ArrayList<Point> points;
	public final Rectangle bbox;
	
	public Glacier(ArrayList<Point> points, Rectangle bbox) {
		this.points = points;
		this.bbox = bbox;
	}
	
	public boolean isPoint() {
		return points.size() < 2;
	}
	public boolean contains(Glacier other) {
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
		if (!(other instanceof Glacier))
			return false;
		Glacier other2 = (Glacier) other;
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
//	public boolean attachEitherEnd(Glacier other) {
//		
//		Point startPoint = points.get(0), endPoint = points.get(points.size() - 1);
//		Point otherStartPoint = other.points.get(0), otherEndPoint = other.points.get(other.points.size() - 1);
//		
//		if (startPoint.equals(otherStartPoint)) {
//			ArrayList<Point> newPoints = new ArrayList<Point>();
//			for (int i = other.points.size() - 1; i > 0; i--) {
//				newPoints.add(other.points.get(i));
//			}
//			for (Point p : points) {
//				newPoints.add(p);
//			}
//			points = newPoints;
//			return true;
//		}
//		if (startPoint.equals(otherEndPoint)) {
//			ArrayList<Point> newPoints = new ArrayList<Point>();
//			for (int i = 0; i < other.points.size() - 1; i++) {
//				newPoints.add(other.points.get(i));
//			}
//			for (Point p : points) {
//				newPoints.add(p);
//			}
//			points = newPoints;
//			return true;
//		}
//		if (endPoint.equals(otherStartPoint)) {
//			for (int i = 1; i < other.points.size(); i++) {
//				points.add(other.points.get(i));
//			}
//			return true;
//		}
//		if (endPoint.equals(otherEndPoint)) {
//			for (int i = other.points.size() - 2; i >= 0; i--) {
//				points.add(other.points.get(i));
//			}
//			return true;
//		}
//		return false;
//		
//	}
	

	public boolean attach(Glacier glacier) {
		Point endPoint = points.get(points.size() - 1), otherStartPoint = glacier.points.get(0);
		if (endPoint.equals(otherStartPoint)) {
			for (int i = 1; i < glacier.points.size(); i++) {
				points.add(glacier.points.get(i));
			}
			return true;
		}
		return false;
	}
	

	

}
