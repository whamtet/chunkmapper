package com.chunkmapper.resourceinfo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class ResourceInfo {
//only contains info, doesn't do anything.
	public final File file;
	public final URL url;
	public final int regionx, regionz;
	
	protected ResourceInfo(String address, File cacheDirectory, String fileName, int regionx, int regionz) {
		this.regionx = regionx;
		this.regionz = regionz;
		file = new File(cacheDirectory, fileName);
		URL url2 = null;
		try {
			url2 = new URL(address);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		url = url2;
	}


}
