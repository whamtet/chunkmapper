package com.chunkmapper.multiplayer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.nbt.NbtIo;

public class MPPointManager {
	//this class a) gives points to the point manager once only
	// b) allows the store to be updated once points have been finished.

	//this is the only field that needs to be synchronized
	private final ArrayList<Point> pointsFinished = new ArrayList<Point>();
	//	private final ArrayList<Point> pointsAssigned = new ArrayList<Point>();
	private final HashSet<Point> pointsAssigned = new HashSet<Point>();
	private final File store;
	public final static int RAD = 3, LON_RAD = 180 * 3600 / 512;
	private final TextDisplay textDisplay;

	public MPPointManager(File chunkmapperFolder, TextDisplay textDisplay, Point rootPoint, File gameFolder) {
		this.textDisplay = textDisplay;
		//need to preliminary add players
		HashMap<String, Point> playerPositions = readPositions(gameFolder);
		//need to update playerPosition
		for (String playerName : playerPositions.keySet()) {
			Point playerPosition = playerPositions.get(playerName);

			int regionx0 = Matthewmatics.div(playerPosition.x, 512);
			int regionz0 = Matthewmatics.div(playerPosition.z, 512);
			
			textDisplay.updatePlayer(playerName, regionx0, regionz0);
		}

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
					textDisplay.addFinishedPoint(new Point(rootPoint.x + p.x, rootPoint.z + p.z));
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static HashMap<String, Point> readPositions(File parentFolder) {
		HashMap<String, Point> out = new HashMap<String, Point>();
		File players = new File(parentFolder, "players");
		for (File f : players.listFiles()) {
			if (f.getName().endsWith(".dat")) {
				String playerName = f.getName().split("\\.")[0];
				out.put(playerName, readPositionSingle(f));
			}
		}
		return out;
	}
	private static Point readPositionSingle(File f) {
		CompoundTag root = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(f));
			root = NbtIo.readCompressed(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CompoundTag Player = root.getCompound("Data").getCompound("Player");
		ListTag<DoubleTag> l = (ListTag<DoubleTag>) root.getList("Pos");
		int x = (int) l.get(0).data, z = (int) l.get(2).data;
		return new Point(x, z);
	}
	private static double getMinDistance(int a, int b, double d) {
		double d2 = Math.sqrt(a*a + b*b);
		return d2 < d ? d2 : d;
	}
	public HashSet<Point> getNewPoints(File gameFolder, Point rootPoint) {
		HashMap<String, Point> playerPositions = readPositions(gameFolder);
		//need to update playerPosition
		HashSet<Point> newPoints = new HashSet<Point>();
		for (String playerName : playerPositions.keySet()) {
			Point playerPosition = playerPositions.get(playerName);

			int regionx0 = Matthewmatics.div(playerPosition.x, 512);
			int regionz0 = Matthewmatics.div(playerPosition.z, 512);
			
			textDisplay.updatePlayer(playerName, regionx0, regionz0);

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
		}
		if (playerPositions.size() == 0) {
			//need to add some default positions
			for (int regionx = -RAD; regionx <= RAD; regionx++) {
				for (int regionz = -RAD; regionz <= RAD; regionz++) {
					newPoints.add(new Point(regionx, regionz));
				}
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
	public static void main(String[] args) throws Exception {
		File parentFolder = new File("/Users/matthewmolloy/Downloads/world");
	}

}
