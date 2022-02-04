package com.chunkmapper.multiplayer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.Point;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.NeutralRegionWriter;

public class MPThread {

	public static void main(String[] args) throws Exception {
		start();
	}
	private static File prepareDir(File f, boolean delete) {
		if (delete && f.exists()) {
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		f.mkdir();
		return f;
	}
	private static double[] getLatLon() {
		System.out.println("Where would you like your map?");

		String name = InputAssistant.readLine("Enter OpenStreetMap URL: ");
		String[] split = name.split("/");
		double[] latlon = new double[2];
		latlon[0] = Double.parseDouble(split[split.length - 2]);
		latlon[1] = Double.parseDouble(split[split.length - 1]);

		return latlon;
	}
	private static void checkNetwork() {
		if (!BucketInfo.initMap()) {
			System.out.println("Chunkmapper could not connect with the internet.");
			System.out.println("Please check your connection and try again.");
			System.exit(0);
		}
	}


	private static void start() throws Exception {
		System.out.println("Welcome to Chunkmapper Multiplayer");
		checkNetwork();
		double lat = 0, lon = 0;
		
		final int verticalExaggeration = 1;
		File gameFolder = new File("world");
		if (!gameFolder.exists()) {
			double[] latlon = getLatLon();
			lat = latlon[0];
			lon = latlon[1];
		}
		System.out.printf("generating from %s, %s\n", lat, lon);
		//write server.properties
		File serverPropertiesFile = new File("server.properties");
		ServerProperties.spitProperties(gameFolder.getName(), serverPropertiesFile);
		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}
		File chunkmapperDir = prepareDir(new File(gameFolder, "chunkmapper"), false);
		File regionFolder = prepareDir(new File(gameFolder, "region"), false);
		prepareDir(new File(gameFolder, "players"), false);

		File metaInfoFile = new File(chunkmapperDir, "meta.txt");
		GameMetaInfo gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon, verticalExaggeration, false); //extra config not supported for now e.g. gaia mode etc

		LocServer.startLocServer(gameMetaInfo.rootPoint, gameFolder);
		MPLevelDat.writeLevelDat(gameFolder, gameFolder.getName());
		
		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		NeutralRegionWriter regionWriter = null;
		TextDisplay textDisplay = new TextDisplay(chunkmapperDir, gameMetaInfo.rootPoint);
		try {
			MPPointManager pointManager = new MPPointManager(chunkmapperDir, textDisplay, gameMetaInfo.rootPoint, gameFolder);
			regionWriter = new NeutralRegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, gameMetaInfo.verticalExaggeration, textDisplay);


			//now we loop for ETERNITY!!!
			while (true) {
				HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, gameMetaInfo.rootPoint);
				for (Point p : pointsToWrite) {

					regionWriter.addTask(p.x, p.z);
				}
//				double minDistance = pointManager.getDistanceToEdge(gameFolder);
//				if (minDistance < 512) {
//					System.err.println("Warning: minDistance " + minDistance);
//				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}


	}

}
