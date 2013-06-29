package com.chunkmapper;

import java.util.HashSet;

public class Interpolator {

	public static HashSet<Point> interpolate(Point p1, Point p2, HashSet<Point> points) {
		int x0 = p1.x, z0 = p1.z;
		int x2 = p2.x, z2 = p2.z;
		
		int xstride = x2 - x0, zstride = z2 - z0;

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
