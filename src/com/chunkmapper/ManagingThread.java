package com.chunkmapper;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.OSMRouter;
import com.chunkmapper.binaryparser.OsmosisParser;
import com.chunkmapper.gui.dialog.MustUpgradeDialog;
import com.chunkmapper.gui.dialog.NoNetworkDialog;
import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.interfaces.GlobalSettings;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.interfaces.PointManager;
import com.chunkmapper.parser.OverpassParser;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.LevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread extends Thread {
	private final double lat, lon;
	private final File gameFolder;
	private final MappedSquareManager mappedSquareManager;
	private final PlayerIconManager playerIconManager;
	private final GlobalSettings globalSettings;
	private final JFrame appFrame;
	private final GeneratingLayer generatingLayer;
	public RegionWriter regionWriter;
	private static boolean networkProblems;
	private static Object networkProblemsGuard = new Object();
	
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
			PlayerIconManager playerIconManager, GlobalSettings globalSettings, JFrame appFrame,
			GeneratingLayer generatingLayer) {
//		if (true) {
//			throw new RuntimeException();
//		}
		this.generatingLayer = generatingLayer;
		this.appFrame = appFrame;
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
	public static void main(String[] args) throws Exception {
		System.out.println(continueWithoutNetwork());
	}
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

	@Override
	public void run() {

		if (globalSettings.isLive()) {
			OSMRouter.setLive();
		}
		generatingLayer.zoomTo();
		System.out.println("generating " + gameFolder.getName());
		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}
		File chunkmapperDir = prepareDir(new File(gameFolder, "chunkmapper"), false);
		File regionFolder = prepareDir(new File(gameFolder, "region"), false);

		File metaInfoFile = new File(chunkmapperDir, "meta.txt");
		GameMetaInfo gameMetaInfo;
		if (metaInfoFile.exists()) {
			try {
				gameMetaInfo = new GameMetaInfo(metaInfoFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
//			} catch (NumberFormatException e) {
//				System.out.println("done");
//				e.printStackTrace();
//				gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon, globalSettings.getVerticalExaggeration());
//			}
		} else {
			gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon, globalSettings.getVerticalExaggeration());
			//is saved automatically
		}
		System.out.println("here");

		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		if (!loadedLevelDatFile.exists()) {
			try {
				//				URL src = ManagingThread.class.getResource("/config/level.dat");
				//				FileUtils.copyURLToFile(src, loadedLevelDatFile);
				LevelDat loadedLevelDat = new LevelDat(loadedLevelDatFile);
				String gameName = gameFolder.getName();
				loadedLevelDat.setName(gameName);
				loadedLevelDat.setPlayerPosition(lon * 3600 - gameMetaInfo.rootPoint.x * 512, 250, - lat * 3600 - gameMetaInfo.rootPoint.z * 512);
				loadedLevelDat.save();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		regionWriter = null;
		try {
			PointManager pointManager = new PointManager(chunkmapperDir, mappedSquareManager, gameMetaInfo.rootPoint);
			regionWriter = new RegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, mappedSquareManager, gameMetaInfo.verticalExaggeration);

			System.out.println("truly starting");
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
			e.printStackTrace();
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
		if (!selfCalled) {
			thread.interrupt();
		}
		thread.regionWriter.blockingShutdownNow();
		while(thread.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//clear out caches.
//		OverpassDownloader.flushCache();
		OsmosisParser.flushCache();

	}

}
