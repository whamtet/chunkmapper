package com.chunkmapper.multiplayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.chunkmapper.MappedSquareManager;
import com.chunkmapper.Point;

public class TextDisplay implements MappedSquareManager {

	private final File chunkmapperDir;
	private HashSet<Point> points = new HashSet<Point>();
	private static final char ZERO = "0".charAt(0), ONE = "1".charAt(0), PLAYER = "*".charAt(0); 
	private final ConcurrentHashMap<String, Point> playerPositions = new ConcurrentHashMap<String, Point>();
	private final Point rootPoint;
	
	public TextDisplay(File chunkmapperDir, Point rootPoint) {
		this.rootPoint = rootPoint;
		this.chunkmapperDir = chunkmapperDir;
	}
	@Override
	public synchronized void addPoint(Point p) {
		points.add(p);
		int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;
		for (Point point : points) {
			if (point.x < minx) minx = point.x;
			if (point.z < minz) minz = point.z;
			if (point.x > maxx) maxx = point.x;
			if (point.z > maxz) maxz = point.z;
		}
		int width = maxx + 1 - minx;
		int height = maxz + 1 - minz;
		char[][] chars = new char[height][width];
		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				chars[z][x] = points.contains(new Point(minx + x, minz + z)) ? ONE : ZERO;
			}
		}
		for (Point playerPosition : playerPositions.values()) {
			if (minx <= playerPosition.x && playerPosition.x < maxx && minz <= playerPosition.z && playerPosition.z < maxz) {
				chars[playerPosition.z - minz][playerPosition.x - minx] = PLAYER;
			}
		}
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(chunkmapperDir, "mappedSquares.txt"))));
			for (int z = 0; z < height; z++) {
				for (int x = 0; x < width; x++) {
					pw.print(chars[z][x]);
				}
				pw.println();
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void updatePlayer(String playerName, int regionx0, int regionz0) {
		playerPositions.put(playerName, new Point(regionx0 + rootPoint.x, regionz0 + rootPoint.z));
		
	}
	


}
