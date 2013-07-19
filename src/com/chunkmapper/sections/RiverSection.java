package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class RiverSection extends Section {
	public final ArrayList<Point> points;
	public final Rectangle bbox;
	public RiverSection(ArrayList<Point> points) {
		this.points = points;
		bbox = null;
	}
	public RiverSection(ArrayList<Point> points2, Rectangle bbox) {
		points = points2;
		this.bbox = bbox;
	}
}
