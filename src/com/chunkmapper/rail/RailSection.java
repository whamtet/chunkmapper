package com.chunkmapper.rail;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.Point;

public class RailSection {
	public final ArrayList<Point> points;
	public final boolean isPreserved, hasBridge, hasCutting, hasEmbankment, hasTunnel;
	public RailSection(ArrayList<Point> points,
			boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel) {
		this.isPreserved = isPreserved;
		this.hasBridge = hasBridge;
		this.hasCutting = hasCutting;
		this.hasEmbankment = hasEmbankment;
		this.hasTunnel = hasTunnel;
		this.points = points;
	}

}
