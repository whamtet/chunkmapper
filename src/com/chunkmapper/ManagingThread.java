package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.rail.HeightsCache;
import com.chunkmapper.writer.LoadedLevelDat;
import com.chunkmapper.writer.RegionWriter;

public class ManagingThread implements Runnable {
	private final double lat, lon;
	private final String gameName;
	private final boolean forceRestart;
	private boolean reteleport;

	public ManagingThread(double lat, double lon, String gameName, boolean forceRestart, boolean reteleport) {
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

		Point rootPoint = getRootPoint(lat, lon, chunkmapperDir);

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
			loadedLevelDat.setPlayerPosition(lon * 3600 - rootPoint.x * 512, 250, - lat * 3600 - rootPoint.z * 512);
		}
		try {
			loadedLevelDat.save();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		HeightsCache.deleteCache();

		//now we need to create all our downloaders;
		PointManager pointManager = new PointManager(chunkmapperDir);
		RegionWriter regionWriter = new RegionWriter(pointManager, rootPoint, regionFolder);

		//now we loop for ETERNITY!!!
		while (true) {
			HashSet<Point> pointsToWrite = pointManager.getNewPoints(gameFolder, rootPoint, chunkmapperDir);
			if (pointsToWrite.size() == 0) {
//				System.out.println("nothing to write now");
			}
			for (Point p : pointsToWrite) {
//				System.out.println(p);
				UberDownloader.addRegionToDownload(p.x + rootPoint.x, p.z + rootPoint.z);
				regionWriter.addTask(p.x, p.z);
			}
			double minDistance = pointManager.getDistanceToEdge(gameFolder);
			if (minDistance < 512) {
				System.err.println("Warning: minDistance " + minDistance);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
//			break;
		}


	}
	private static Point getRootPoint(double lat, double lon, File chunkmapperDir) {
		//gets a root point, saving it as necessary.
		File store = new File(chunkmapperDir, "roots.txt");
		Point out = null;
		if (store.exists()) {
			//read it in
			try {
				BufferedReader reader = new BufferedReader(new FileReader(store));
				int x = Integer.parseInt(reader.readLine());
				int z = Integer.parseInt(reader.readLine());
				out = new Point(x, z);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (out == null) {
			int x = (int) Math.floor(lon * 3600 / 512);
			int z = (int) Math.floor(-lat * 3600 / 512);
			out = new Point(x, z);
			System.out.println(out);
			//and write it back
			try {
				PrintWriter writer = new PrintWriter(store);
				writer.println(x);
				writer.println(z);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;

	}
	//	private static void recordRoots(File chunkmapperDir, int rootRegionx, int rootRegionz)

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
//		File f = new File("/Library/Caches/Chunkmapper2");
//		if (f.exists()) {
//			System.out.println("deleting cache");
//			FileUtils.deleteDirectory(f);
//		}
		
//		double[] latlon = geocode.core.placeToCoords("london");
		double[] latlon = geocode.core.placeToCoords("nelson, nz");
//		double[] latlon = getLatLon(); //get last recorded place
		boolean forceReload = true;
		boolean reteleport = false;
		ManagingThread thread = new ManagingThread(latlon[0], latlon[1], "world", forceReload, reteleport);
		thread.run();

	}

}
