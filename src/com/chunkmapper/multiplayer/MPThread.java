package com.chunkmapper.multiplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.MappedSquareManager;
import com.chunkmapper.Point;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.LoadedLevelDat;
import com.chunkmapper.writer.NeutralRegionWriter;

public class MPThread {

	public static void main(String[] args) throws Exception {
		//for fresh restart
//		File f = new File("/Users/matthewmolloy/images"), g = new File("/Users/matthewmolloy/.chunkmappermp");
//		FileUtils.deleteDirectory(g);
//		g.mkdir();
//		FileUtils.copyDirectory(f, new File(g, "images"));
//		start(new String[] {"nelson, nz"});
		start(args);
	}
	private static void start(String[] args) throws Exception {
		//wowa, just put in a location
		double[] latlon = Nominatim.getPoint(args[0]);
		run(latlon[0], latlon[1]);
	}
//	private static void start(String[] args) throws Exception {
//	
//		HashMap<String, String> cli = new HashMap<String, String>();
//		for (int i = 0; i < args.length; i += 2) {
//			cli.put(args[i], args[i+1]);
//		}
//		if (!cli.containsKey("-lat") || !cli.containsKey("-lon") || !cli.containsKey("-name")) {
//			System.out.println("usage: -lat -lon -name");
//			System.exit(0);
//		}
//		double lat = Double.parseDouble(cli.get("-lat"));
//		double lon = Double.parseDouble(cli.get("-lon"));
//		String name = cli.get("-name");
//		//we're going to throw everything in current dir.
//		File gameFolder = new File(name);
//		int verticalExaggeration = 1;
//		run(lat, lon, gameFolder, verticalExaggeration);
//	}
	private static File prepareDir(File f, boolean delete) {
		if (delete && f.exists()) {
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!f.exists()) {
			f.mkdir();
		}
		return f;
	}

	private static void run(double lat, double lon) throws IOException {
		System.out.printf("generating from %s, %s", lat, lon);
		final int verticalExaggeration = 1;
		File gameFolder = new File("world");
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
		GameMetaInfo gameMetaInfo;
		if (metaInfoFile.exists()) {
			try {
				gameMetaInfo = new GameMetaInfo(metaInfoFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		} else {
			gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon, verticalExaggeration);
		}

		MPLevelDat.writeLevelDat(gameFolder, gameFolder.getName());
		
		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		NeutralRegionWriter regionWriter = null;
		UberDownloader uberDownloader = null;
		TextDisplay textDisplay = new TextDisplay(chunkmapperDir, gameMetaInfo.rootPoint);
		try {
			MPPointManager pointManager = new MPPointManager(chunkmapperDir, textDisplay, gameMetaInfo.rootPoint, gameFolder);
			uberDownloader = new UberDownloader();
			regionWriter = new NeutralRegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, uberDownloader, gameMetaInfo.verticalExaggeration, textDisplay);


			//now we loop for ETERNITY!!!
			while (true) {
				HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, gameMetaInfo.rootPoint);
				for (Point p : pointsToWrite) {

					uberDownloader.addRegionToDownload(p.x + gameMetaInfo.rootPoint.x, p.z + gameMetaInfo.rootPoint.z);
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
			if (uberDownloader != null)
				uberDownloader.shutdown();
			if (regionWriter != null)
				regionWriter.blockingShutdownNow();
			return;
		}


	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */

}
