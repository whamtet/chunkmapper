package com.chunkmapper.resourceinfo;

import java.io.File;

import com.chunkmapper.admin.Utila;

public class GlobcoverResourceInfo extends ResourceInfo {
	public static final String FORMAT_URL = 
			"http://gdi.geo.hu-berlin.de/cgi-bin/mapserv?map=/data/gdi_geohu/admin/globcover_2009/GLOBCOVER_L4_200901_200912_V2.3.color.map&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=512&HEIGHT=512&LAYERS=GLOBCOVER+L4+200901+200912+V2.3+color&STYLES=default&TRANSPARENT=TRUE&FORMAT=image/png&bbox=%s,%s,%s,%s&width=51&height=51";
	public static final File CACHE_DIRECTORY = new File(Utila.CACHE, "globcover");
	static {
		if (!CACHE_DIRECTORY.exists())
			CACHE_DIRECTORY.mkdirs();
	}
	public static final int PICTURE_SIZE = 51;

	public GlobcoverResourceInfo(int regionx, int regionz) {
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GlobcoverResourceInfo info = new GlobcoverResourceInfo(1173, 320);
		System.out.println(info.url);

	}

}
