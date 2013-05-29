package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.XapiRailResourceInfo;

public class XapiRailDownloader extends XmlDownloader {

	@Override
	protected void download(Point task) throws Exception {
		XapiRailResourceInfo fileToDownload = new XapiRailResourceInfo(task.x, task.z);
		super.downloadXml(fileToDownload);
	}


}
