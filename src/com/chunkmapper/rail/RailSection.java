package com.chunkmapper.rail;

import java.util.ArrayList;

import com.chunkmapper.Point;

public class RailSection {
	public final boolean allowAscend, allowDescend;
	public final ArrayList<Point> points;
	public final boolean isPreserved;
	public RailSection(ArrayList<Point> points, boolean allowAscend, boolean allowDescend, boolean isPreserved) {
		this.isPreserved = isPreserved;
		this.allowAscend = allowAscend;
		this.allowDescend = allowDescend;
		this.points = points;
	}
}
