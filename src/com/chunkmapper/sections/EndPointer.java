package com.chunkmapper.sections;

import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.Point;

public class EndPointer {
	private HashSet<Point> points = new HashSet<Point>(), endPoints = new HashSet<Point>();
	
	public EndPointer(ArrayList<Point> interpPoints) {
		//remove the last point
		interpPoints.remove(interpPoints.size() - 1);
		
		Point firstPoint = interpPoints.get(0), lastPoint = interpPoints.get(interpPoints.size() - 1);
		for (int i = 0; i < interpPoints.size(); i++) {
			Point p2 = i == interpPoints.size() - 1 ? firstPoint : interpPoints.get(i + 1);
			interpolate(interpPoints.get(i), p2, points);
		}
		
		for (int i = 0; i < interpPoints.size(); i++) {
			Point previousPoint = i == 0 ? lastPoint : interpPoints.get(i - 1);
			Point point = interpPoints.get(i);
			Point nextPoint = i == interpPoints.size() - 1 ? firstPoint : interpPoints.get(i + 1);
			if (point.z < previousPoint.z && point.z < nextPoint.z
					|| point.z > previousPoint.z && point.z > nextPoint.z) {
				//is an endPoint.  Extend to both sides
				for (int x = point.x; true; x++) {
					Point provisionalPoint = new Point(x, point.z);
					if (points.contains(provisionalPoint)) {
						endPoints.add(provisionalPoint);
					} else {
						break;
					}
				}
				for (int x = point.x - 1; true; x--) {
					Point provisionalPoint = new Point(x, point.z);
					if (points.contains(provisionalPoint)) {
						endPoints.add(provisionalPoint);
					} else {
						break;
					}
				}
			}
		}
	}
	public boolean isEndPoint(int x, int z) {
		return endPoints.contains(new Point(x, z));
	}
	public boolean isPoint(int x, int z) {
		return points.contains(new Point(x, z));
	}

	private static HashSet<Point> interpolate(Point p1, Point p2, HashSet<Point> points) {
		int x0 = p1.x, z0 = p1.z;
		int x2 = p2.x, z2 = p2.z;
		
		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			points.add(new Point(x0, z0));
			return points;
		}

		int width = xstride >= 0 ? xstride : -xstride;
		int height = zstride >= 0 ? zstride : -zstride;
		int xstep = xstride >= 0 ? 1 : -1;
		int zstep = zstride >= 0 ? 1 : -1;
		
		if (width >= height) {
			for (int i = 0; i <= width; i++) {
				int x = x0 + i * xstep;
				int z = z0 + i * zstride / width;
				points.add(new Point(x, z));
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i * zstep;
				points.add(new Point(x, z));
			}
		}
		return points;
	}
}
