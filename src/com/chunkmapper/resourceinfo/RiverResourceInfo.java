package com.chunkmapper.resourceinfo;

import java.io.File;

import com.chunkmapper.Utila;

public class RiverResourceInfo extends ResourceInfo {

	public static final String FORMAT_URL = "http://www2.demis.nl/WMS/wms.ashx?wms=WorldMap&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=512&HEIGHT=512&LAYERS=Rivers&STYLES=default&TRANSPARENT=TRUE&FORMAT=image/gif&bbox=%s,%s,%s,%s";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "demisRivers");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}

	public RiverResourceInfo(int regionx, int regionz) {
		super(getAddress(regionx, regionz), CACHE_DIRECTORY, "f_" + regionx + "_" + regionz + "_512" + ".png", regionx, regionz);
		// TODO Auto-generated constructor stub
	}
	private static String getAddress(int regionx, int regionz) {
		final double REGION_WIDTH_IN_DEGREES = 512 / 3600.;
		double lon1 = regionx * REGION_WIDTH_IN_DEGREES;
		double lon2 = lon1 + REGION_WIDTH_IN_DEGREES;
		double lat2 = -regionz * REGION_WIDTH_IN_DEGREES;
		double lat1 = lat2 - REGION_WIDTH_IN_DEGREES;

		return String.format(FORMAT_URL, lon1, lat1, lon2, lat2);
	}
	
	

}
