package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;

public class GlobcoverDownloader extends PngDownloader {

	@Override
	protected void download(Point task) throws Exception {
		super.downloadPng(new GlobcoverResourceInfo(task.x, task.z));
	}


}
