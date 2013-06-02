package com.chunkmapper.downloader;

public class UberDownloader {
//	private staticFlightgearRailDownloader flightgearRailDownloader = new FlightgearRailDownloader();
	private GlobcoverDownloader globcoverDownloader = new GlobcoverDownloader();
	private LakeDownloader lakeDownloader = new LakeDownloader();
	private NoaaGshhsDownloader noaaGshhsDownloader = new NoaaGshhsDownloader();
	private RiverDownloader riverDownloader = new RiverDownloader();
	private XapiDownloader xapiDownloader = new XapiDownloader();
	public HeightsDownloader heightsDownloader = new HeightsDownloader();
	private XapiRailDownloader xapiRailDownloader = new XapiRailDownloader();
	
//	public static void redownloadXapi(int regionx, int regionz) {
//		xapiRailDownloader.addForceDownload(regionx, regionz);
//	}

	public void addRegionToDownload(int regionx, int regionz) {
		System.out.println("adding region " + regionx + ", " + regionz);
		//obligatory sources
		int numAttempts = -1;
		globcoverDownloader.addTask(regionx, regionz);
		noaaGshhsDownloader.addTask(regionx, regionz);
		heightsDownloader.addTask(regionx, regionz);
		
		//third time lucky
//		numAttempts = 3;
		lakeDownloader.addTask(regionx, regionz);
		riverDownloader.addTask(regionx, regionz);
		xapiDownloader.addTask(regionx, regionz);
		xapiRailDownloader.addTask(regionx, regionz);
	}
	public void shutdown() {
		globcoverDownloader.shutdown();
		lakeDownloader.shutdown();
		noaaGshhsDownloader.shutdown();
		riverDownloader.shutdown();
		xapiDownloader.shutdown();
		heightsDownloader.shutdown();
		xapiRailDownloader.shutdown();
	}

}
