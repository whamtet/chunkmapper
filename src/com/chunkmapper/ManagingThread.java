package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.gui.swing.GameInfoPanel;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.LoadedLevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread extends Thread {
	private final double lat, lon;
	private final String gameName;
	private final boolean forceRestart;
	private boolean reteleport;
	private final GameInfoPanel gameInfoPanel;

	public ManagingThread(double lat, double lon, String gameName, boolean forceRestart, boolean reteleport, GameInfoPanel gameInfoPanel) {
		this.gameInfoPanel = gameInfoPanel;
		this.lat = lat;
		this.lon = lon;
		this.gameName = gameName;
		this.forceRestart = forceRestart;
		this.reteleport = reteleport;
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
		File gameFolder = prepareDir(new File(Utila.SAVES_FOLDER, gameName), this.forceRestart);
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
		} else {
			gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon);
		}

		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		if (!loadedLevelDatFile.exists()) {
			File src = new File("resources/level.dat");
			reteleport = true;
			try {
				FileUtils.copyFile(src, loadedLevelDatFile);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		LoadedLevelDat loadedLevelDat = new LoadedLevelDat(loadedLevelDatFile);
		if (reteleport) {
			loadedLevelDat.setName(gameName);
			loadedLevelDat.setPlayerPosition(lon * 3600 - gameMetaInfo.rootPoint.x * 512, 250, - lat * 3600 - gameMetaInfo.rootPoint.z * 512);
		}
		try {
			loadedLevelDat.save();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		RegionWriter regionWriter = null;
		UberDownloader uberDownloader = null;
		try {
			PointManager pointManager = new PointManager(chunkmapperDir);
			uberDownloader = new UberDownloader();
			ProgressManager progressManager = null;
			if (gameInfoPanel != null)
				progressManager = gameInfoPanel.progressManager;
			regionWriter = new RegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, progressManager, uberDownloader);


			//now we loop for ETERNITY!!!
			while (true) {
				HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, gameMetaInfo.rootPoint, chunkmapperDir);
				if (pointsToWrite.size() == 0) {
					//				System.out.println("nothing to write now");
				}
				for (Point p : pointsToWrite) {
					if (gameInfoPanel != null) {
						gameInfoPanel.progressManager.incrementTotalTasks();
					}

					uberDownloader.addRegionToDownload(p.x + gameMetaInfo.rootPoint.x, p.z + gameMetaInfo.rootPoint.z);
					regionWriter.addTask(p.x, p.z);
				}
				double minDistance = pointManager.getDistanceToEdge(gameFolder);
				if (minDistance < 512) {
					System.err.println("Warning: minDistance " + minDistance);
				}
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
	public static double[] getLatLon() throws NumberFormatException, IOException {
		double[] out = new double[2];
		BufferedReader reader = new BufferedReader(new FileReader("location.txt"));
		out[0] = Double.parseDouble(reader.readLine());
		out[1] = Double.parseDouble(reader.readLine());
		reader.close();
		return out;
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("granity, nz");
		//		double[] latlon = getLatLon(); //get last recorded place
		boolean forceReload = false;
		boolean reteleport = false;
		ManagingThread thread = new ManagingThread(latlon[0], latlon[1], "world", forceReload, reteleport, null);
		thread.start();
	}
	public static void blockingShutDown(ManagingThread thread) {
		thread.interrupt();
		while(thread.isAlive()) {
			//			System.out.println("waiting shutdown");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
