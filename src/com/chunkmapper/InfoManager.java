package com.chunkmapper;
import geocode.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoManager {
	public static File globcoverFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "globcover");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File heightsFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "heights");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File lakesFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "lakes");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File poiFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "poi");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File riversFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "rivers");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File boundariesFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "boundaries");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File coastlinesFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "coastlines");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}
	public static File railsFile(int regionx, int regionz) {
		File parent = new File(Utila.CACHE, "rails");
		parent.mkdirs();
		File f = new File(parent, "f_" + regionx + "_" + regionz + Utila.BINARY_SUFFIX);
		return f;
	}

	public static URL globcoverServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL heightsServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL lakesServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[natural=water][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL poiServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?node[place=*][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL riversServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[waterway=river][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL boundariesServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[boundary=administrative][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL coastlinesServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[natural=coastline][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}
	public static URL railsServer(int regionx, int regionz) throws MalformedURLException {
		String s = "http://www.overpass-api.de/api/xapi?way[railway=*][bbox=%s,%s,%s,%s]";
		return new URL(getAddress(s, regionx, regionz));
	}

	public static URL globcoverBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL heightsBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL lakesBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL poiBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL riversBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL boundariesBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL coastlinesBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}
	public static URL railsBackup(int regionx, int regionz) throws MalformedURLException {
		return null;
	}

	private static String getAddress(String s, int regionx, int regionz) {
		final double REGION_WIDTH_IN_DEGREES = 512 / 3600.;
		double lon1 = regionx * REGION_WIDTH_IN_DEGREES;
		double lon2 = lon1 + REGION_WIDTH_IN_DEGREES;
		double lat2 = -regionz * REGION_WIDTH_IN_DEGREES;
		double lat1 = lat2 - REGION_WIDTH_IN_DEGREES;

		return String.format(s, lon1, lat1, lon2, lat2);
	}
	public static void main(String[] args) {
		double[] latlon = core.placeToCoords("new plymouth, nz");
	}

}
