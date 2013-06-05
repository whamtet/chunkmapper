package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.XapiRiverResourceInfo;

public class XapiRiverDownloader extends XmlDownloader {
	@Override
	protected void download(Point task) throws Exception {
		XapiRiverResourceInfo fileToDownload = new XapiRiverResourceInfo(task.x, task.z);
		super.downloadXml(fileToDownload);
	}
}
