package com.chunkmapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.math.Matthewmatics;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;

public class PointManager {
	//this class a) gives points to the point manager once only
	// b) allows the store to be updated once points have been finished.

	//this is the only field that needs to be synchronized
	private final ArrayList<Point> pointsFinished = new ArrayList<Point>();
	//	private final ArrayList<Point> pointsAssigned = new ArrayList<Point>();
	private final HashSet<Point> pointsAssigned = new HashSet<Point>();
	private final File store;
	public final static int RAD = 2, LON_RAD = 180 * 3600 / 512;

	public PointManager(File chunkmapperFolder, MappedSquareManager mappedSquareManager, Point rootPoint) {

		store = new File(chunkmapperFolder, "regionsMade.txt");
		if (store.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(store));
				String line;
				while ((line = reader.readLine()) != null) {
					String[] s = line.split(" ");
					Point p = new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
					pointsFinished.add(p);
					pointsAssigned.add(p);
					mappedSquareManager.addPoint(new Point(p.x + rootPoint.x, p.z + rootPoint.z));
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static Point readPosition(File parentFolder) {
		CompoundTag data = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(new File(parentFolder, "level.dat")));
			data = NbtIo.readCompressed(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CompoundTag Player = data.getCompound("Data").getCompound("Player");
		ListTag<DoubleTag> l = (ListTag<DoubleTag>) Player.getList("Pos");
		int x = (int) l.get(0).data, z = (int) l.get(2).data;
		return new Point(x, z);
	}
	private static double getMinDistance(int a, int b, double d) {
		double d2 = Math.sqrt(a*a + b*b);
		return d2 < d ? d2 : d;
	}
	public double getDistanceToEdge(File gameFolder) {
		Point playerPosition = readPosition(gameFolder);
		double minDistance = Double.MAX_VALUE;

		//need to get list of waiting-to-write tiles
		HashSet<Point> waitingToWrite = new HashSet<Point>();
		for (Point p : this.pointsAssigned) {
			waitingToWrite.add(p);
		}
		synchronized(this) {
			for (Point p : this.pointsFinished) {
				waitingToWrite.remove(p);
			}
		}
		for (Point p : waitingToWrite) {
			minDistance = getMinDistance(p.x - playerPosition.x, p.z - playerPosition.z, minDistance);
			minDistance = getMinDistance(p.x - playerPosition.x - 512, p.z - playerPosition.z, minDistance);
			minDistance = getMinDistance(p.x - playerPosition.x, p.z - playerPosition.z - 512, minDistance);
			minDistance = getMinDistance(p.x - playerPosition.x - 512, p.z - playerPosition.z - 512, minDistance);
		}
		return minDistance;
	}
	private static void updateIconManager(Point playerPosition, Point rootPoint, PlayerIconManager playerIconManager) {
		double lon = (playerPosition.x + rootPoint.x*512) / 3600.;
		double lat = -(playerPosition.z + rootPoint.z * 512) / 3600.;
		playerIconManager.setLocation(lat, lon);
	}
	public HashSet<Point> getNewPoints(File gameFolder, Point rootPoint, File chunkmapperDir, PlayerIconManager playerIconManager) {
		Point playerPosition = readPosition(gameFolder);
		updateIconManager(playerPosition, rootPoint, playerIconManager);
		int regionx0 = Matthewmatics.div(playerPosition.x, 512);
		int regionz0 = Matthewmatics.div(playerPosition.z, 512);

		return getSurroundingPoints(regionx0, regionz0);
	}
//	private static void recordLink(Point playerPosition, Point rootPoint, File chunkmapperDir) {
//		//writes an html file with a 
//		double lon = (playerPosition.x + rootPoint.x*512) / 3600., lat = -(playerPosition.z + rootPoint.z*512) / 3600.;
//		String s = "<a href=\"https://maps.google.com.au/?q=loc:%s+%s\" target=\"_blank\">View Google map<a />";
//		s = String.format(s, lat, lon);
//		try {
//			FileWriter writer = new FileWriter(new File(chunkmapperDir, "location.html"));
//			writer.write(s);
//			writer.close();
//			
//			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("location.txt")));
//			pw.println(lat);
//			pw.println(lon);
//			pw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	private HashSet<Point> getSurroundingPoints(int regionx0, int regionz0) {

		HashSet<Point> newPoints = new HashSet<Point>();
		int regionx1 = regionx0 - RAD, regionx2 = regionx0 + RAD;
		int regionz1 = regionz0 - RAD, regionz2 = regionz0 + RAD;
		if (regionx1 <= -LON_RAD) regionx1 = -LON_RAD + 1;
		if (regionz1 <= -LON_RAD / 2) regionz1 = -LON_RAD / 2 + 1;
		if (regionx2 >= LON_RAD) regionx2 = LON_RAD - 1;
		if (regionz2 >= LON_RAD / 2) regionz2 = LON_RAD / 2 - 1;
		
		for (int regionx = regionx1; regionx <= regionx2; regionx++) {
			for (int regionz = regionz1; regionz <= regionz2; regionz++) {
				newPoints.add(new Point(regionx, regionz));
			}
		}

		for (Point p : pointsAssigned) {
			newPoints.remove(p);
		}
		for (Point p : newPoints) {
			pointsAssigned.add(p);
		}
		return newPoints;
	}

	public synchronized void updateStore(Point p) {
		pointsFinished.add(p);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(store));
			for (Point p2 : pointsFinished) {
				writer.write(p2.x + " " + p2.z + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//	public static void main(String[] args) {
	//		PointManager manager = new PointManager(new File("."));
	//		System.out.println(manager.getSurroundingPoints(0, 0));
	//	}

}
