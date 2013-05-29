package com.chunkmapper;

import java.io.File;

import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;
import com.chunkmapper.resourceinfo.LakeResourceInfo;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;
import com.chunkmapper.resourceinfo.RiverResourceInfo;
import com.chunkmapper.resourceinfo.XapiRailResourceInfo;
import com.chunkmapper.resourceinfo.XapiResourceInfo;

public class CheckResources {
	public static void checkResource(double lat, double lon) {
		int regionx = (int) Math.floor(lon * 3600 / 512);
		int regionz = (int) Math.floor(-lat * 3600 / 512);
		
		XapiRailResourceInfo railInfo = new XapiRailResourceInfo(regionx, regionz);
		System.out.println(railInfo.url);
	}
	public static void printUnavailableResources(double lat, double lon) {
		int regionx = (int) Math.floor(lon * 3600 / 512);
		int regionz = (int) Math.floor(-lat * 3600 / 512);
		
		File f;
		
		f = (new GlobcoverResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		
		f = (new HeightsResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		
		f = (new LakeResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		
		f = (new NoaaGshhsResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		

		
		f = (new RiverResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		
		f = (new XapiRailResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
		
		f = (new XapiResourceInfo(regionx, regionz)).file;
		if (!f.exists())
			System.out.println(f);
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("london");
		checkResource(latlon[0], latlon[1]);
//		CheckResources.printUnavailableResources(latlon[0], latlon[1]);
		System.out.println(latlon[0] + ", " + latlon[1]);
	}

}
