package com.chunkmapper.resourceinfo;

import geocode.core;

import java.io.File;

import com.chunkmapper.Utila;


public class XapiRailResourceInfo extends ResourceInfo {
	public static final String FORMAT_URL = "http://www.overpass-api.de/api/xapi?way[railway=rail%spreserved][bbox=%s,%s,%s,%s]";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "xapirail");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}

	public XapiRailResourceInfo(int regionx, int regionz) {
		super(getAddress(regionx, regionz), CACHE_DIRECTORY, "f_" + regionx + "_" + regionz + ".xml", regionx, regionz);
		// TODO Auto-generated constructor stub
	}
	private static String getAddress(int regionx, int regionz) {
		final double REGION_WIDTH_IN_DEGREES = 512 / 3600.;
		double lon1 = regionx * REGION_WIDTH_IN_DEGREES;
		double lon2 = lon1 + REGION_WIDTH_IN_DEGREES;
		double lat2 = -regionz * REGION_WIDTH_IN_DEGREES;
		double lat1 = lat2 - REGION_WIDTH_IN_DEGREES;

		return String.format(FORMAT_URL, "%7C", lon1, lat1, lon2, lat2);
	}

public static  void main(String[] args) {
	double[] latlon = core.placeToCoords("kingston, nz");
	int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
	int regionz = (int) Math.floor(latlon[0] * 3600 / -512);
	XapiRailResourceInfo info = new XapiRailResourceInfo(regionx, regionz);
	System.out.println(info.url);
}

	/**
	 * @param args
	 */

}
