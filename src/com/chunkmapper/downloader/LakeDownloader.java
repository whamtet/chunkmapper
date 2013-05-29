package com.chunkmapper.downloader;

import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.LakeResourceInfo;

public class LakeDownloader extends PngDownloader {

	@Override
	protected void download(Point task) throws Exception {
		super.downloadPng(new LakeResourceInfo(task.x, task.z));

	}


}
