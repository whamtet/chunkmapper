package com.chunkmapper.resourceinfo;

import java.io.File;

import com.chunkmapper.Utila;

public class HeightsResourceInfo extends ResourceInfo {
	public static final String FORMAT_URL = "http://data.worldwind.arc.nasa.gov/elev?service=WMS&request=GetMap&version=1.3&crs=CRS:84&layers=mergedAsterElevations&styles=&format=application/bil16&width=%s&height=%s&bbox=%s,%s,%s,%s";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "heights");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}
	public static final int LEN = 512 + 2 * Utila.CHUNK_START;
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
		final double PADDING_IN_DEGREES = Utila.CHUNK_START / 3600.;
//		final String FORMAT_URL = 

		lon1 -= PADDING_IN_DEGREES; lat1 -= PADDING_IN_DEGREES;
		lon2 += PADDING_IN_DEGREES; lat2 += PADDING_IN_DEGREES;
		return String.format(FORMAT_URL, LEN, LEN, lon1, lat1, lon2, lat2);
	}
	public static void main(String[] args) throws Exception {
		HeightsResourceInfo info = new HeightsResourceInfo(1, 1);
		System.out.println(info.file);
		System.out.println(info.url);
		System.out.println(info.checkLength);
	}

}
