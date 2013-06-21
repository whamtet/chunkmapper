package com.chunkmapper.protoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;

public class ServerInfoManager {
	private static ServerInfo serverInfo;
	
	public static void main(String[] args) throws Exception {
		ServerInfo info = getServerInfo();
		System.out.println(info.getRailAddress());
	}
	private static void writeToStagingDirectory() throws IOException {
		ServerInfo.Builder builder = ServerInfo.newBuilder();
		builder.setRailAddress("http://chunkmapper-static.appspot.com/myrails/");
		
		File outFile = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/ServerInfo.pbf");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(builder.build().toByteArray());
		out.close();
		
		System.out.println("done");
	}
	public static synchronized ServerInfo getServerInfo() throws IOException {
		if (serverInfo == null)
			serverInfo = doGetServerInfo();
		return serverInfo;
	}
	private static ServerInfo doGetServerInfo() throws IOException {
		URL url = new URL("http://chunkmapper-static.appspot.com/ServerInfo.pbf");
		
		InputStream in = url.openStream();
		ServerInfo info = ServerInfo.parseFrom(in);
		in.close();
		
		return info;
	}
	

}
