package com.chunkmapper.interfaces;

import java.io.File;
import java.util.HashSet;

import com.chunkmapper.Point;

public interface PointManager {

	public final static int LON_RAD = 180 * 3600 / 512;
	public static final String REGIONS_MADE = "regionsMade.txt";

	/*
	 * Returns the shortest distance along the x or z axes from the player to the edge of the generated map.
	 */
	public double getDistanceToEdge(File gameFolder);

	/*
	 * Returns a box of (regionx, regionz) points to be generated.
	 */
	public HashSet<Point> getNewPoints(File gameFolder, Point rootPoint,
			File chunkmapperDir, PlayerIconManager playerIconManager);

	/*
	 * Saves (pointX, pointZ) as completed.
	 */
	public void flagPointCompletion(Point p);
	
	public Point getCurrentPlayerPosition();

}