package com.chunkmapper.protoc;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.chunkmapper.protoc.RectangleContainer.RectangleList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;


public class RailInfoManager {
	private static ArrayList<Rectangle> railRectangles;
	
	public static synchronized ArrayList<Rectangle> getRailRectangles() throws IOException {
		if (railRectangles == null)
			railRectangles = doGetRailRectangles();
		return railRectangles;
	}
	
	private static ArrayList<Rectangle> doGetRailRectangles() throws IOException {
		ArrayList<Rectangle> out = new ArrayList<Rectangle>();
		
		ServerInfo info = ServerInfoManager.getServerInfo();
		URL url = new URL(info.getRailAddress() + "master.pbf");
		InputStream in = url.openStream();
		RectangleList rectangleList = RectangleList.parseFrom(in);
		in.close();
		
		for (RectangleContainer.Rectangle rectangle : rectangleList.getRectanglesList()) {
			out.add(new Rectangle(rectangle.getX(), rectangle.getZ(), rectangle.getWidth(), rectangle.getHeight()));
		}
		
		return out;
	}
	
	private static void writeToStagingDirectory() throws IOException {
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrails");
		RectangleList.Builder listBuilder = RectangleList.newBuilder();
		
		for (File f : parent.listFiles()) {
			if (f.getName().startsWith("f_")) {
				String[] split = f.getName().split("_");
				int x = Integer.parseInt(split[1]);
				int z = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				
				
				listBuilder.addRectangles(RectangleContainer.Rectangle.newBuilder()
						.setX(x)
						.setZ(z)
						.setWidth(width)
						.setHeight(height).build());
			}
		}
		File outFile = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/master.pbf");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(listBuilder.build().toByteArray());
		out.close();
		
		System.out.println("done");
	}
	
	public static void main(String[] args) throws Exception {
		writeToStagingDirectory();
	}

}
