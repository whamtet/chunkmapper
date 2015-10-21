package com.chunkmapper;

public class Point extends java.awt.Point implements Comparable<Point> {
	
	/*
	 * The same as java.awt.Point but stores the second coordinate as z instead of y;
	 * This is because the Minecraft coordinate system is (x, y) instead of (x, z);
	 */
	
	public final int z;
	
	public Point(int x, int z) {
		super(x, z);
		this.z = z;
	}
	public Point(double d, double e) {
		this((int) d, (int) e);
	}
	
	public static Point getRegionPoint(int x, int z) {
		return new Point(com.chunkmapper.math.Matthewmatics.div(x, 512), com.chunkmapper.math.Matthewmatics.div(z, 512));
	}
	
	public static Point getRegionPoint(double lat, double lon) {
		int x = (int) Math.floor(lon * 3600 / 512);
		int z = (int) Math.floor(-lat * 3600 / 512);
		return new Point(x, z);
	}
	
	@Override
	public int compareTo(Point arg0) {
		return (new Integer(x)).compareTo(arg0.x);
	}

}
