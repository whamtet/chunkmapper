package com.chunkmapper.downloader;

import com.chunkmapper.downloader.binary.BinaryRailsDownloader;

public class UberDownloader {
	private GlobcoverDownloader globcoverDownloader = new GlobcoverDownloader();
//	private LakeDownloader lakeDownloader = new LakeDownloader();
//	private NoaaGshhsDownloader noaaGshhsDownloader = new NoaaGshhsDownloader();
//	private RiverDownloader riverDownloader = new RiverDownloader();
//	private XapiRiverDownloader riverDownloader = new XapiRiverDownloader();
//	private XapiDownloader xapiDownloader = new XapiDownloader();
	public HeightsDownloader heightsDownloader = new HeightsDownloader();
	
//	public static void redownloadXapi(int regionx, int regionz) {
//		xapiRailDownloader.addForceDownload(regionx, regionz);
//	}

	public void addRegionToDownload(int regionx, int regionz) {
		//obligatory sources
		globcoverDownloader.addTask(regionx, regionz);
//		noaaGshhsDownloader.addTask(regionx, regionz);
		heightsDownloader.addTask(regionx, regionz);
		
		//third time lucky
//		numAttempts = 3;
//		lakeDownloader.addTask(regionx, regionz);
//		riverDownloader.addTask(regionx, regionz);
//		xapiDownloader.addTask(regionx, regionz);
//		xapiRailDownloader.addTask(regionx, regionz);
	}
	public void shutdown() {
		globcoverDownloader.shutdownNow();
//		lakeDownloader.shutdownNow();
//		noaaGshhsDownloader.shutdownNow();
//		riverDownloader.shutdownNow();
//		xapiDownloader.shutdownNow();
		heightsDownloader.shutdownNow();
//		xapiRailDownloader.shutdownNow();
	}

}
