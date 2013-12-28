package com.chunkmapper;

public class Point extends java.awt.Point implements Comparable<Point> {
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
//	public static Point getPoint(double lat, double lon) {
//		int x = (int) (lon * 3600);
//		int z = (int) (-lat * 3600);
//		return new Point(x, z);
//	}
	
//	public static PointContainer.Point getProtocPoint(double lat, double lon)
	

	public final int z;
	public Point(int x, int z) {
		super(x, z);
		this.z = z;
	}
//	public Point(com.chunkmapper.protoc.PointContainer.Point p) {
//		super(p.getX(), p.getZ());
//		this.z = p.getZ();
//	}
	@Override
	public int compareTo(Point arg0) {
		return (new Integer(x)).compareTo(arg0.x);
	}

}
