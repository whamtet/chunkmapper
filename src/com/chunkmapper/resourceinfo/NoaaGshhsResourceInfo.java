package com.chunkmapper.resourceinfo;

import java.io.File;

import com.chunkmapper.Utila;

public class NoaaGshhsResourceInfo extends ResourceInfo {

	public static final String FORMAT_URL = "http://maps.ngdc.noaa.gov/arcgis/services/web_mercator/gshhs/MapServer/WmsServer?VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=512&HEIGHT=512&LAYERS=GSHHS_f_L1&STYLES=default&TRANSPARENT=TRUE&FORMAT=image/png&bbox=%s,%s,%s,%s";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "noaaGshhs");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}

	public NoaaGshhsResourceInfo(int regionx, int regionz) {
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
	
	
//	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("brisbane");
//		int regionx = (int) (latlon[1] * 3600 / 512), regionz = -(int) (latlon[0] * 3600 / 512);
//		NoaaGshhsResourceInfo info = new NoaaGshhsResourceInfo(regionx+1, regionz-1);
//		System.out.println(info.url);
//	}

}
