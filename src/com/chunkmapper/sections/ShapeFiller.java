package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class ShapeFiller {
	private static double getAngle(Point p1, Point p2) {
		if (p1.equals(p2)) {
			return 0;
		}
		int dx = p2.x - p1.x;
		int dz = p2.z - p1.z;
		if (dx == 0) {
			return dz > 0 ? Math.PI / 2 : 3 * Math.PI / 2;
		}
		
		double theta = Math.atan2(dz, dx);
		if (dx < 0) {
			return theta + Math.PI;
		}
		if (theta < 0) {
			theta += 2 * Math.PI;
		}
		return theta;
	}
	private static boolean isCollinear(Point a, Point b, Point c) {
		double t1 = Math.atan2(b.z - a.z, b.x - a.x);
		double t2 = Math.atan2(c.z - a.z, c.x - a.x);
		return t1 == t2;
	}
	private static Point getInternalPoint(Point a, Point b, Point c) {
		int dx = a.x + c.x - 2 * b.x;
		int dz = a.z + c.z - 2 * b.z;
		
		int dxd = dx, dzd = dz;
		while (dxd != 0 || dzd != 0) {
			dx = dxd; dz = dzd;
			dxd /= 2; dzd /= 2;
		}
		return new Point(b.x + dx, b.z + dz);
	}
	public static void main(String[] args) {
		System.out.println(getInternalPoint(new Point(0, 100), new Point(0, 0), new Point(10, 0)));
	}

}
