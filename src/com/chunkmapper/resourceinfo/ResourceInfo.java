package com.chunkmapper.resourceinfo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.chunkmapper.admin.MyLogger;

public abstract class ResourceInfo {
//only contains info, doesn't do anything.
	public final File file;
	public final URL url, url2;
	public final int regionx, regionz;
	
	protected ResourceInfo(String address, File cacheDirectory, String fileName, int regionx, int regionz) {
		this.regionx = regionx;
		this.regionz = regionz;
		file = new File(cacheDirectory, fileName);
		URL urld = null;
		try {
			urld = new URL(address);
		} catch (MalformedURLException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
		}
		url = urld;
		url2 = null;
	}
	
	protected ResourceInfo(String address, String address2, File cacheDirectory, String fileName, int regionx, int regionz) {
		this.regionx = regionx;
		this.regionz = regionz;
		file = new File(cacheDirectory, fileName);
		URL urld = null;
		try {
			urld = new URL(address);
		} catch (MalformedURLException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
		}
		url = urld;
		try {
			urld = new URL(address2);
		} catch (MalformedURLException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
		}
		url2 = urld;
	}
	


}
