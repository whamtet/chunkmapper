package com.chunkmapper.downloader;

public class UberDownloader {
//	private staticFlightgearRailDownloader flightgearRailDownloader = new FlightgearRailDownloader();
	private static GlobcoverDownloader globcoverDownloader = new GlobcoverDownloader();
	private static LakeDownloader lakeDownloader = new LakeDownloader();
	private static NoaaGshhsDownloader noaaGshhsDownloader = new NoaaGshhsDownloader();
	private static RiverDownloader riverDownloader = new RiverDownloader();
	private static XapiDownloader xapiDownloader = new XapiDownloader();
	public static HeightsDownloader heightsDownloader = new HeightsDownloader();
	private static XapiRailDownloader xapiRailDownloader = new XapiRailDownloader();
	
//	public static void redownloadXapi(int regionx, int regionz) {
//		xapiRailDownloader.addForceDownload(regionx, regionz);
//	}

	public static void addRegionToDownload(int regionx, int regionz) {
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

}
