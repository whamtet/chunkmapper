package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.XapiResourceInfo;

public class XapiDownloader extends XmlDownloader {

	@Override
	protected void download(Point task) throws Exception {
		XapiResourceInfo fileToDownload = new XapiResourceInfo(task.x, task.z);
		super.downloadXml(fileToDownload);
	}


}
