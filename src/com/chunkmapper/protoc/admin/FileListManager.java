package com.chunkmapper.protoc.admin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;


public class FileListManager {
	public static final FileList railFileList, riverFileList;
	
	static {
		FileList railFileList2 = null, riverFileList2 = null;
		
		try {
			ServerInfo info = ServerInfoManager.serverInfo;
			
			URL url = new URL(info.getRailAddress() + "master.pbf");
			InputStream in = url.openStream();
			railFileList2 = FileList.parseFrom(in);
			in.close();
			
			url = new URL(info.getRiverAddress() + "master.pbf");
			in = url.openStream();
			riverFileList2 = FileList.parseFrom(in);
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		railFileList = railFileList2;
		riverFileList = riverFileList2;
		
	}


}
