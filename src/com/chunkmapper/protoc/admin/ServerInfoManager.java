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
	
	//method synchronized locks on entire object, so be careful here!
	public static synchronized ServerInfo getServerInfo() {
		if (serverInfo == null) {
			serverInfo = doGetServerInfo();
		}
		return serverInfo;
	}

	private static ServerInfo doGetServerInfo() {
		ServerInfo info = null;
		try {
			URL url = new URL("http://backend.chunkmapper.com/static/ServerInfo.pbf");

			InputStream in = new BufferedInputStream(url.openStream());
			info = ServerInfo.parseFrom(in);
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	private static void writeToStagingDirectory() throws IOException {
		ServerInfo.Builder builder = ServerInfo.newBuilder();
		builder.setRailAddress("http://chunkmapper-static.appspot.com/public/myrails/");
		builder.setRiverAddress("http://chunkmapper-static.appspot.com/public/myrivers/");
		builder.setLakeAddress("http://chunkmapper-static.appspot.com/public/mylakes/");
		builder.setCoastlineAddress("http://chunkmapper-static.appspot.com/public/mycoastlines/");
		builder.setGlobcoverAddress("http://chunkmapper-static.appspot.com/public/mat/");
		builder.setGetXapi(true);
		builder.setResetPasswordAddress("http://www.chunkmapper.com/reset-password");
		
		File outFile = new File("/Users/matthewmolloy/python/helloworld/static/ServerInfo.pbf");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(builder.build().toByteArray());
		out.close();

		System.out.println("done");
	}

	public static void main(String[] args) throws Exception {
		writeToStagingDirectory();
	}
}
