package com.chunkmapper;



public class Point extends java.awt.Point {
//	public static Point pointFromCoordinate(Coordinate c) {
//		return new Point((int) (c.x * 3600), (int) -(c.y * 3600));
//	}
	public static Point getRegionPoint(int x, int z) {
		return new Point(com.chunkmapper.math.Matthewmatics.div(x, 512), com.chunkmapper.math.Matthewmatics.div(z, 512));
	}
	public static Point getRegionPoint(double lat, double lon) {
		int x = (int) Math.floor(lon * 3600 / 512);
		int z = (int) Math.floor(-lat * 3600 / 512);
		return new Point(x, z);
	}
	

	public final int z;
	public Point(int x, int z) {
		super(x, z);
		this.z = z;
	}

}
