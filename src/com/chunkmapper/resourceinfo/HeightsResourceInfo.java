package com.chunkmapper.resourceinfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.chunkmapper.Utila;
import com.chunkmapper.downloader.HeightsDownloader;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.HeightsReaderImpl;

public class HeightsResourceInfo extends ResourceInfo {
	public static final String FORMAT_URL = "http://data.worldwind.arc.nasa.gov/elev?service=WMS&request=GetMap&version=1.3&crs=CRS:84&layers=mergedAsterElevations&styles=&format=application/bil16&width=%s&height=%s&bbox=%s,%s,%s,%s";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "heights");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}
	public static final int LEN = 512 + 2 * Utila.HEIGHTS_START;
	public static final int FILE_LENGTH = LEN*LEN*2;
	public final int checkLength = FILE_LENGTH;
	
	public HeightsResourceInfo(int regionx, int regionz) {
		super(getAddress(regionx, regionz), CACHE_DIRECTORY, "f_" + regionx + "_" + regionz + "_" + LEN, regionx, regionz);
	}
	private static String getAddress(int regionx, int regionz) {
		final double REGION_WIDTH_IN_DEGREES = 512 / 3600.;
		double lon1 = regionx * REGION_WIDTH_IN_DEGREES;
		double lon2 = lon1 + REGION_WIDTH_IN_DEGREES;
		double lat2 = -regionz * REGION_WIDTH_IN_DEGREES;
		double lat1 = lat2 - REGION_WIDTH_IN_DEGREES;

		//adjust for padding
		final double PADDING_IN_DEGREES = Utila.HEIGHTS_START / 3600.;
//		final String FORMAT_URL = 

		lon1 -= PADDING_IN_DEGREES; lat1 -= PADDING_IN_DEGREES;
		lon2 += PADDING_IN_DEGREES; lat2 += PADDING_IN_DEGREES;
		return String.format(FORMAT_URL, LEN, LEN, lon1, lat1, lon2, lat2);
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = {-39.59, 174.42};
		int chunkx = (int) Math.floor(latlon[1] * 3600 / 512);
		int chunkz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		download(chunkx, chunkz);
		deleteAndExit(chunkx, chunkz);
		HeightsReader reader = new HeightsReaderImpl(chunkx, chunkz);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				pw.println(reader.getRealHeightij(i, j));
			}
		}
		pw.close();
	}
	private static void download(int chunkx, int chunkz) throws InterruptedException {
		HeightsDownloader downloader = new HeightsDownloader();
		downloader.addTask(chunkx, chunkz);
	}
	private static void deleteAndExit(int chunkx, int chunkz) throws IOException {
		String fileName = "/Library/Caches/Chunkmapper/heights/f_" + chunkx + "_" + chunkz + "_552";
		File f = new File(fileName);
		System.out.println(f.exists());
//		Runtime.getRuntime().exec("rm " + fileName);
//		Runtime.getRuntime().exec("rm " + fileName + "~");
//		System.exit(0);
	}

}
