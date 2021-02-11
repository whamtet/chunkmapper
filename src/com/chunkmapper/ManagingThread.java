package com.chunkmapper;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.ui.NewGameInfo;
import com.chunkmapper.gui.dialog.NoNetworkDialog;
import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.interfaces.PointManager;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.LevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread extends Thread {
	private final double lat, lon;
	private final File gameFolder;
	private final MappedSquareManager mappedSquareManager;
	private final PlayerIconManager playerIconManager;
	private final GeneratingLayer generatingLayer;
	public RegionWriter regionWriter;
	private final NewGameInfo newGameInfo;
	private static boolean networkProblems;
	private static Object networkProblemsGuard = new Object();

	{
		setName("Managing Thread");
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
			PlayerIconManager playerIconManager,
			GeneratingLayer generatingLayer, NewGameInfo newGameInfo) {
		clearNetworkProblems();

		this.newGameInfo = newGameInfo;
		this.generatingLayer = generatingLayer;
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

		if (generatingLayer != null)
			generatingLayer.zoomTo();

		MyLogger.LOGGER.info(String.format("Generating %s at %s, %s", gameFolder.getName(), lat, lon));

		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}

		File chunkmapperDir = prepareDir(new File(gameFolder, "chunkmapper"), false);
		File regionFolder = prepareDir(new File(gameFolder, "region"), false);

		GameMetaInfo gameMetaInfo = null;
		try {
			boolean isGaia = newGameInfo == null ? false : newGameInfo.isGaia;
			gameMetaInfo = new GameMetaInfo(gameFolder, lat, lon, 1, isGaia);
			gameMetaInfo.save();
		} catch (IOException e1) {
			MyLogger.LOGGER.severe(MyLogger.printException(e1));
		}
		File levelDatFile = new File(gameFolder, "level.dat");
		LevelDat levelDat = null;
		try {
			levelDat = new LevelDat(levelDatFile, newGameInfo);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			MyLogger.LOGGER.severe(MyLogger.printException(e1));
			return;
		}
		String gameName = gameFolder.getName();
		levelDat.setName(gameName);
		//need to set altitude correctly.
		int altitude = 128;
		int absx = (int) (lon * 3600);
		int absz = (int) (-lat * 3600);
		levelDat.setPlayerPosition(absx - gameMetaInfo.rootPoint.x * 512, altitude, absz - gameMetaInfo.rootPoint.z * 512);
		levelDat.save();

		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		regionWriter = null;
		try {
			PointManager pointManager;
//			if (globalSettings.nz) {
//				globalSettings.nz = false;
//				pointManager = new BoundedPointManager(chunkmapperDir, mappedSquareManager, gameMetaInfo.rootPoint);
//			} else {
				pointManager = new PointManagerImpl(chunkmapperDir, mappedSquareManager, gameMetaInfo.rootPoint);
//			}

			regionWriter = new RegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, mappedSquareManager, gameMetaInfo.isGaia, 1,
					levelDat
					);

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
			return;
		}


	}

	public static double[] getLatLon() throws NumberFormatException, IOException {
		double[] out = new double[2];
		BufferedReader reader = new BufferedReader(new FileReader("location.txt"));
		out[0] = Double.parseDouble(reader.readLine());
		out[1] = Double.parseDouble(reader.readLine());
		reader.close();
		return out;
	}

}
