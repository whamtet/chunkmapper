package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class POI {
	public final String text;
	public final Point point;
	public POI(Point point, String text) {
		this.text = text;
		this.point = point;
	}
	public int hashCode() {
		return point.hashCode();
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof POI)) {
			return false;
		}
		return ((POI) other).point.equals(point);
	}

}
