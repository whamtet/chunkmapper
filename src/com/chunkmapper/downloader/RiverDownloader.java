package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.RiverResourceInfo;

public class RiverDownloader extends PngDownloader {

	@Override
	protected void download(Point task, boolean useBackupServer) throws Exception {
		super.downloadPng(new RiverResourceInfo(task.x, task.z));
	}


}
