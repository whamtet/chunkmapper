package com.chunkmapper.protoc.admin;

import java.io.File;
import java.io.FileInputStream;

import com.chunkmapper.protoc.FileContainer.FileList;


public class OfflineFileListManager {
	public static final FileList railFileList, riverFileList, lakeFileList;
	
	static {
		railFileList = getFileList(new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/master.pbf"));
		riverFileList = getFileList(new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrivers/master.pbf"));
		lakeFileList = getFileList(new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/mylakes/master.pbf"));
	}
	
	private static FileList getFileList(File f) {
		try {
			byte[] data = new byte[(int) f.length()];
			FileInputStream in = new FileInputStream(f);
			in.read(data);
			in.close();
			
			return FileList.parseFrom(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
