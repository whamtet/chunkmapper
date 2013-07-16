package com.chunkmapper.protoc.admin;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;


public class FileListManager {
	private static FileList railFileList, riverFileList, lakeFileList, coastlinesFileList, globcoverFileList;

	private static FileList getFileList(URL url) {
		try {
			InputStream in = new BufferedInputStream(url.openStream());
			return FileList.parseFrom(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) throws Exception {
		System.out.println(getRailFileList().getFilesCount());
	}
	
	public static synchronized FileList getCoastlinesFileList() throws MalformedURLException {
		if (coastlinesFileList == null) {
			ServerInfo info = ServerInfoManager.getServerInfo();
			coastlinesFileList = getFileList(new URL(info.getCoastlineAddress() + "master.pbf"));
		}
		return coastlinesFileList;
	}
	public static synchronized FileList getGlobcoverFileList() throws MalformedURLException {
		if (globcoverFileList == null) {
			ServerInfo info = ServerInfoManager.getServerInfo();
			globcoverFileList = getFileList(new URL(info.getGlobcoverAddress() + "master.pbf"));
		}
		return globcoverFileList;
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
