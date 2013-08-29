package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.gui.LoginDialog;
import com.chunkmapper.gui.SuspiciousPasswordDialog;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.security.SecurityManager;
import com.chunkmapper.writer.LoadedLevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread extends Thread {
	private final double lat, lon;
	private final File gameFolder;
	private final MappedSquareManager mappedSquareManager;
	private final PlayerIconManager playerIconManager;
	private final int verticalExaggeration;
	private final JFrame appFrame;
	private final GeneratingLayer generatingLayer;
	
	public ManagingThread(double lat, double lon, File gameFolder, MappedSquareManager mappedSquareManager,
			PlayerIconManager playerIconManager, int verticalExaggeration, JFrame appFrame,
			GeneratingLayer generatingLayer) {
		
		this.generatingLayer = generatingLayer;
		this.appFrame = appFrame;
		this.verticalExaggeration = verticalExaggeration;
		this.mappedSquareManager = mappedSquareManager;
		this.playerIconManager = playerIconManager;
		this.lat = lat;
		this.lon = lon;
		this.gameFolder = gameFolder;
	}
	public static void main(String[] args) throws Exception {
		URL src = ManagingThread.class.getResource("/config/level.dat");
		System.out.println(src);
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
		//first need to check security
//		if (!SecurityManager.isOfflineValid()) {
//			String email = null;
//			boolean isLoggedIn = false;
//			while(!isLoggedIn) {
//				LoginDialog dialog = new LoginDialog(appFrame, email);
//				dialog.setVisible(true);
//				if (dialog.cancelled) {
//					generatingLayer.cancel();
//					return;
//				}
//				email = dialog.getEmail();
//				switch(SecurityManager.onlineValidity(email, dialog.getPassword())) {
//				case SecurityManager.REQUIRES_LOGIN:
//				(new SuspiciousPasswordDialog(appFrame)).setVisible(true);
//				break;
//				case SecurityManager.VALID:
//				isLoggedIn = true;
//				break;
//				}
//			}
//		}
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
		} else {
			gameMetaInfo = new GameMetaInfo(metaInfoFile, lat, lon, verticalExaggeration);
		}

		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		if (!loadedLevelDatFile.exists()) {
			try {
				URL src = ManagingThread.class.getResource("/config/level.dat");
				FileUtils.copyURLToFile(src, loadedLevelDatFile);
				LoadedLevelDat loadedLevelDat = new LoadedLevelDat(loadedLevelDatFile);
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
		RegionWriter regionWriter = null;
		UberDownloader uberDownloader = null;
		try {
			PointManager pointManager = new PointManager(chunkmapperDir, mappedSquareManager, gameMetaInfo.rootPoint);
			uberDownloader = new UberDownloader();
			regionWriter = new RegionWriter(pointManager, gameMetaInfo.rootPoint, regionFolder, 
					gameMetaInfo, mappedSquareManager, uberDownloader, gameMetaInfo.verticalExaggeration);


			//now we loop for ETERNITY!!!
			while (true) {
				HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, gameMetaInfo.rootPoint, chunkmapperDir, playerIconManager);
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
	public static double[] getLatLon() throws NumberFormatException, IOException {
		double[] out = new double[2];
		BufferedReader reader = new BufferedReader(new FileReader("location.txt"));
		out[0] = Double.parseDouble(reader.readLine());
		out[1] = Double.parseDouble(reader.readLine());
		reader.close();
		return out;
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
