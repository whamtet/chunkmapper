package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;

public class NoaaGshhsDownloader extends PngDownloader {

	@Override
	protected void download(Point task, boolean useBackupServer) throws Exception {
		super.downloadPng(new NoaaGshhsResourceInfo(task.x, task.z));
	}


}
