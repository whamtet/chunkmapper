package com.chunkmapper;

import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;
import com.chunkmapper.resourceinfo.LakeResourceInfo;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;
import com.chunkmapper.resourceinfo.RiverResourceInfo;
import com.chunkmapper.resourceinfo.XapiRailResourceInfo;
import com.chunkmapper.resourceinfo.XapiResourceInfo;

public class PurgeCache {
	//purges some of the cache
	private static void purge(int regionx, int regionz) {
		(new GlobcoverResourceInfo(regionx, regionz)).file.delete();
		(new NoaaGshhsResourceInfo(regionx, regionz)).file.delete();
		(new HeightsResourceInfo(regionx, regionz)).file.delete();
		(new LakeResourceInfo(regionx, regionz)).file.delete();
		(new RiverResourceInfo(regionx, regionz)).file.delete();
		(new XapiRailResourceInfo(regionx, regionz)).file.delete();
		(new XapiResourceInfo(regionx, regionz)).file.delete();
		
//		noaaGshhsDownloader.addTask(regionx, regionz, numAttempts);
//		heightsDownloader.addTask(regionx, regionz, numAttempts);
//		
//		//third time lucky
////		numAttempts = 3;
//		lakeDownloader.addTask(regionx, regionz, numAttempts);
//		riverDownloader.addTask(regionx, regionz, numAttempts);
//		xapiDownloader.addTask(regionx, regionz, numAttempts);
//		xapiRailDownloader.addTask(regionx, regionz, numAttempts);
//		xapiDownloader.addForceDownload(regionx, regionz);
//		xapiRailDownloader.addForceDownload(regionx, regionz);
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = ManagingThread.getLatLon();
		int regionx0 = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz0 = (int) Math.floor(-latlon[0] * 3600 / 512);
		
		int rad = 2;
		for (int regionx = regionx0 - rad; regionx <= regionx0 + rad; regionx++) {
			for (int regionz = regionz0 - rad; regionz <= regionz0 + rad; regionz++) {
				PurgeCache.purge(regionx, regionz);
			}
		}
		System.out.println("done");
	}

}
