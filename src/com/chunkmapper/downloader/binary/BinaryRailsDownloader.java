package com.chunkmapper.downloader.binary;

import java.io.File;

import com.chunkmapper.Point;
import com.chunkmapper.Utila;
import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.FileListManager;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.protoc.wrapper.RailRegionBuilder;

public class BinaryRailsDownloader extends BinaryDownloader {

	@Override
	protected void download(Point p, boolean useBackupServer) throws Exception {
		FileList fileList = FileListManager.getRailFileList();
		ServerInfo info = ServerInfoManager.getServerInfo();
		String rootURL = info.getRailAddress() + "data/";
		RailRegionBuilder regionBuilder = new RailRegionBuilder();
		File parentFile = new File(Utila.CACHE, "myrails");
		parentFile.mkdirs();
		super.doDownload(p, fileList, rootURL, regionBuilder, parentFile);
	}

}
