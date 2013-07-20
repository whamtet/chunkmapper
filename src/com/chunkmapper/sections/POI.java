package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class POI extends Section {
	public final String text, type;
	public final Point point;
	public final Integer population;
	public POI(Point point, String text, Integer population, String type) {
		this.text = text;
		this.point = point;
		this.population = population;
		this.type = type;
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
	public String toString() {
		return String.format("%s at %s, %s: population %s", text, point.x, point.z, population);
	}

}
