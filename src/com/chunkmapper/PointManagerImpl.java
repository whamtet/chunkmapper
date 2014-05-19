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
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.interfaces.PointManager;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.nbt.NbtIo;

public class PointManagerImpl implements PointManager {
	//this class a) gives points to the point manager once only
	// b) allows the store to be updated once points have been finished.

	//this is the only field that needs to be synchronized
	private final ArrayList<Point> pointsFinished = new ArrayList<Point>();
	//	private final ArrayList<Point> pointsAssigned = new ArrayList<Point>();
	private final HashSet<Point> pointsAssigned = new HashSet<Point>();
	private final ArrayList<Point> refreshPoints = new ArrayList<Point>();
	private final File store;
	private volatile Point currentPlayerPosition;
	private final MappedSquareManager mappedSquareManager;
	private final GlobalSettings globalSettings;
	
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/Alps/chunkmapper");
		Point rootPoint = new Point(0, 0);
		PointManager m = new PointManagerImpl(f, null, rootPoint, null);
		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/Alps");
		System.out.println(m.getNewPoints(gameFolder, rootPoint, f, null).size());
	}

	public Point getCurrentPlayerPosition() {
		return currentPlayerPosition;
	}

	public PointManagerImpl(File chunkmapperFolder, MappedSquareManager mappedSquareManager, Point rootPoint, GlobalSettings globalSettings) {
		this.mappedSquareManager = mappedSquareManager;
		this.globalSettings = globalSettings;
		store = new File(chunkmapperFolder, "regionsMade.txt");
		if (store.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(store));
				String line;
				while ((line = reader.readLine()) != null) {
					String[] s = line.split(" ");
					Point p = new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
					if (globalSettings != null && globalSettings.refreshNext) {
						refreshPoints.add(p);
						
					} else {
						pointsFinished.add(p);
						pointsAssigned.add(p);
						if (mappedSquareManager != null)
							mappedSquareManager.addFinishedPoint(new Point(p.x + rootPoint.x, p.z + rootPoint.z));
					}
				}
				reader.close();
			} catch (IOException e) {
				MyLogger.LOGGER.warning(MyLogger.printException(e));
			} catch (NumberFormatException e) {
				MyLogger.LOGGER.warning(MyLogger.printException(e));
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
	/* (non-Javadoc)
	 * @see com.chunkmapper.interfaces.PointManager#getDistanceToEdge(java.io.File)
	 */
	@Override
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
		if (playerIconManager != null) 
			playerIconManager.setLocation(lat, lon);
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.interfaces.PointManager#getNewPoints(java.io.File, com.chunkmapper.Point, java.io.File, com.chunkmapper.interfaces.PlayerIconManager)
	 */
	@Override
	public HashSet<Point> getNewPoints(File gameFolder, Point rootPoint, File chunkmapperDir, PlayerIconManager playerIconManager) {
		Point playerPosition = readPosition(gameFolder);
		//need to update playerPosition
		currentPlayerPosition = playerPosition;
		updateIconManager(playerPosition, rootPoint, playerIconManager);
		int regionx0 = Matthewmatics.div(playerPosition.x, 512);
		int regionz0 = Matthewmatics.div(playerPosition.z, 512);

		return getSurroundingPoints(regionx0, regionz0, rootPoint);
	}

	private HashSet<Point> getSurroundingPoints(int regionx0, int regionz0, Point rootPoint) {

		HashSet<Point> newPoints = new HashSet<Point>();
		int regionx1 = regionx0 - globalSettings.generationRadius, regionx2 = regionx0 + globalSettings.generationRadius;
		int regionz1 = regionz0 - globalSettings.generationRadius, regionz2 = regionz0 + globalSettings.generationRadius;
		if (regionx1 <= -LON_RAD) regionx1 = -LON_RAD + 1;
		if (regionz1 <= -LON_RAD / 2) regionz1 = -LON_RAD / 2 + 1;
		if (regionx2 >= LON_RAD) regionx2 = LON_RAD - 1;
		if (regionz2 >= LON_RAD / 2) regionz2 = LON_RAD / 2 - 1;

		for (int regionx = regionx1; regionx <= regionx2; regionx++) {
			for (int regionz = regionz1; regionz <= regionz2; regionz++) {
//				newPoints.add(new Point(regionx, regionz));
				Point p = new Point(regionx, regionz);
				if (!pointsAssigned.contains(p)) {
					pointsAssigned.add(p);
					mappedSquareManager.addUnfinishedPoint(new Point(p.x + rootPoint.x, p.z + rootPoint.z));
					newPoints.add(p);
				}
			}
		}

		if (globalSettings.refreshNext) {
			globalSettings.refreshNext = false;
			for (Point p : refreshPoints) {
				pointsAssigned.add(p);
				mappedSquareManager.addUnfinishedPoint(new Point(p.x + rootPoint.x, p.z + rootPoint.z));
				newPoints.add(p);
			}
		}
		return newPoints;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.interfaces.PointManager#updateStore(com.chunkmapper.Point)
	 */
	@Override
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

}
