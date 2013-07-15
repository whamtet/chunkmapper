package com.chunkmapper.protoc.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;

public class ServerInfoManager {
	private static ServerInfo serverInfo;
	
	public static synchronized ServerInfo getServerInfo() {
		if (serverInfo == null) {
			serverInfo = doGetServerInfo();
		}
		return serverInfo;
	}

	private static ServerInfo doGetServerInfo() {
		ServerInfo info = null;
		try {
			URL url = new URL("http://chunkmapper-static.appspot.com/public/ServerInfo.pbf");

			InputStream in = new BufferedInputStream(url.openStream());
			info = ServerInfo.parseFrom(in);
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static void main(String[] args) throws Exception {
		writeToStagingDirectory();
	}
	private static void writeToStagingDirectory() throws IOException {
		ServerInfo.Builder builder = ServerInfo.newBuilder();
		builder.setRailAddress("http://chunkmapper-static.appspot.com/myrails/");
		builder.setRiverAddress("http://chunkmapper-static.appspot.com/myrivers/");
		builder.setLakeAddress("http://chunkmapper-static.appspot.com/mylakes/");
		File outFile = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/ServerInfo.pbf");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(builder.build().toByteArray());
		out.close();

		System.out.println("done");
	}


}
