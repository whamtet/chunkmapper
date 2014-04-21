package com.chunkmapper.interfaces;

import java.io.File;
import java.util.HashSet;

import com.chunkmapper.Point;

public interface PointManager {

	public final static int LON_RAD = 180 * 3600 / 512;
	public final static int RAD = 3;
	public static final String REGIONS_MADE = "regionsMade.txt";

	public double getDistanceToEdge(File gameFolder);

	public HashSet<Point> getNewPoints(File gameFolder, Point rootPoint,
			File chunkmapperDir, PlayerIconManager playerIconManager);

	public void updateStore(Point p);

	public Point getCurrentPlayerPosition();

}