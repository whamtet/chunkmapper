package com.chunkmapper;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.OSMRouter;
import com.chunkmapper.binaryparser.OsmosisParser;
import com.chunkmapper.gui.dialog.NoNetworkDialog;
import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.interfaces.PointManager;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.HeightsReaderS3;
import com.chunkmapper.security.MySecurityManager;
import com.chunkmapper.writer.LevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread extends Thread {
	private final double lat, lon;
	private final File gameFolder;
	private final MappedSquareManager mappedSquareManager;
	private final PlayerIconManager playerIconManager;
	private final GlobalSettings globalSettings;
	private final GeneratingLayer generatingLayer;
	public RegionWriter regionWriter;
	private static boolean networkProblems;
	private static Object networkProblemsGuard = new Object();

	public static void main(String[] args) throws MalformedURLException, URISyntaxException, IOException {
		double[] latlon = Nominatim.getPoint("Hollywood");
		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/Hollywood");
		GlobalSettings globalSettings = new GlobalSettings();
		ManagingThread t = new ManagingThread(latlon[0], latlon[1], gameFolder, null, null, globalSettings, null);
		t.run();
	}

	public static void setNetworkProblems() {
		synchronized(networkProblemsGuard) {
			networkProblems = true;
		}
	}

	private static boolean hasNetworkProblems() {
		synchronized(networkProblemsGuard) {
			return networkProblems;
		}
	}

	private static void clearNetworkProblems() {
		synchronized(networkProblemsGuard) {
			networkProblems = false;
		}
	}

	public ManagingThread(double lat, double lon, File gameFolder, MappedSquareManager mappedSquareManager,
			PlayerIconManager playerIconManager, GlobalSettings globalSettings,
			GeneratingLayer generatingLayer) {
		clearNetworkProblems();

		//		if (true) {
		//			throw new RuntimeException();
		//		}
		this.generatingLayer = generatingLayer;
		this.globalSettings = globalSettings;
		this.mappedSquareManager = mappedSquareManager;
		this.playerIconManager = playerIconManager;
		this.lat = lat;
		this.lon = lon;
		this.gameFolder = gameFolder;
	}
	private static boolean continueWithoutNetwork() {
		NoNetworkDialog d = new NoNetworkDialog();
		d.setModalityType(ModalityType.APPLICATION_MODAL);
		d.setVisible(true);
		return d.continueGeneration;
	}
	private static File prepareDir(File f, boolean delete) {
		if (delete && f.exists()) {
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				MyLogger.LOGGER.warning(MyLogger.printException(e));
			}
		}
		if (!f.exists()) {
			f.mkdir();
		}
		return f;
	}

	@Override
	public void run() {

		if (globalSettings.isLive()) {
			OSMRouter.setLive();
		}
		if (generatingLayer != null)
			generatingLayer.zoomTo();
		
		MyLogger.LOGGER.info(String.format("Generating %s at %s, %s", gameFolder.getName(), lat, lon));
		MyLogger.LOGGER.info("Vertical Exaggeration: " + globalSettings.getVerticalExaggeration());
		MyLogger.LOGGER.info("Live Mode: " + globalSettings.isLive());

		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}
		File chunkmapperDir = prepareDir(new File(gameFolder, "chunkmapper"), false);
		File regionFolder = prepareDir(new File(gameFolder, "region"), false);

		GameMetaInfo gameMetaInfo = null;
		try {
			gameMetaInfo = new GameMetaInfo(gameFolder, lat, lon, globalSettings.getVerticalExaggeration());
		} catch (IOException e1) {
			MyLogger.LOGGER.severe(MyLogger.printException(e1));
		}

		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		try {
			LevelDat loadedLevelDat = new LevelDat(loadedLevelDatFile);
			String gameName = gameFolder.getName();
			loadedLevelDat.setName(gameName);
			//need to set altitude correctly.
			int absx = 0, absz = 0, altitude;
			try {
				absx = (int) (lon * 3600);
				absz = (int) (-lat * 3600);
				if (!MySecurityManager.offlineValid)
					absz = Matthewmatics.div(absz, 128) * 128 + 64;
				int regionx = Matthewmatics.div(absx, 512), regionz = Matthewmatics.div(absz, 512);
				HeightsReader heightsReader = new HeightsReaderS3(regionx, regionz, gameMetaInfo.verticalExaggeration);
				altitude = heightsReader.getHeightxz(absx, absz) + 20;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				MyLogger.LOGGER.info(MyLogger.printException(e));
				return;
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				MyLogger.LOGGER.warning(MyLogger.printException(e));
				altitude = 250;
			}
			loadedLevelDat.setPlayerPosition(absx - gameMetaInfo.rootPoint.x * 512, altitude, absz - gameMetaInfo.rootPoint.z * 512);
			loadedLevelDat.save();
		} catch (IOException e) {
			MyLogger.LOGGER.warning((MyLogger.printException(e)));
			return;
		}

		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		regionWriter = null;
		try {
			PointManager pointManager = new PointManager(chunkmapperDir, mappedSquareManager, gameMetaInfo.rootPoint);
			regionWriter = new RegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, mappedSquareManager, globalSettings.gaiaMode, globalSettings.getVerticalExaggeration());

			MyLogger.LOGGER.info("truly starting");
			//now we loop for ETERNITY!!!
			while (true) {
				HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, gameMetaInfo.rootPoint, chunkmapperDir, playerIconManager);
				for (Point p : pointsToWrite) {
					regionWriter.addTask(p.x, p.z);
				}

				Thread.sleep(1000);
				if (hasNetworkProblems()) {
					if (continueWithoutNetwork()) {
						clearNetworkProblems();
					} else {
						generatingLayer.cancel(true);
						return;
					}
				}
			}
		} catch (InterruptedException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
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
	public static double[] getLatLon() throws NumberFormatException, IOException {
		double[] out = new double[2];
		BufferedReader reader = new BufferedReader(new FileReader("location.txt"));
		out[0] = Double.parseDouble(reader.readLine());
		out[1] = Double.parseDouble(reader.readLine());
		reader.close();
		return out;
	}
	public static void blockingShutDown(ManagingThread thread, boolean selfCalled) {
		MyLogger.LOGGER.info("Starting to shut down thread.");
		if (!selfCalled) {
			thread.interrupt();
		}
		if (thread.regionWriter != null)
			thread.regionWriter.blockingShutdownNow();
		if (!selfCalled) {
			while(thread.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					MyLogger.LOGGER.warning(MyLogger.printException(e));
				}
			}
		}
		OsmosisParser.flushCache();
//		POIParser.flushCache();
		MyLogger.LOGGER.info("shut down thread");

	}

}
