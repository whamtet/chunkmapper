package com.chunkmapper.protoc.admin;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;


public class FileListManager {
	private static FileList railFileList, riverFileList, lakeFileList;
	
	private static FileList getFileList(URL url) {
		try {
		InputStream in = url.openStream();
		return FileList.parseFrom(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized FileList getRailFileList() throws MalformedURLException {
		if (railFileList == null) {
			ServerInfo info = ServerInfoManager.getServerInfo();
			railFileList = getFileList(new URL(info.getRailAddress() + "master.pbf"));
		}
		return railFileList;
	}
	public static synchronized FileList getRiverFileList() throws MalformedURLException {
		if (riverFileList == null) {
			ServerInfo info = ServerInfoManager.getServerInfo();
			riverFileList = getFileList(new URL(info.getRiverAddress() + "master.pbf"));
		}
		return riverFileList;
	}
	public static synchronized FileList getLakeFileList() throws MalformedURLException {
		if (lakeFileList == null) {
			ServerInfo info = ServerInfoManager.getServerInfo();
			lakeFileList = getFileList(new URL(info.getLakeAddress() + "master.pbf"));
		}
		return lakeFileList;
	}


}
