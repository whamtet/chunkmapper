package com.chunkmapper.sections;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class Lake {
	public ArrayList<Point> points;
	public final Rectangle bbox;
	public final boolean isInner, isCove, isLagoon;
	
	public Lake(ArrayList<Point> points, Rectangle bbox, boolean isInner, boolean isCove, boolean isLagoon) {
		this.points = points;
		this.bbox = bbox;
		this.isInner = isInner;
		this.isCove = isCove;
		this.isLagoon = isLagoon;
	}

}
