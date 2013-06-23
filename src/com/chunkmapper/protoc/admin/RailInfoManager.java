package com.chunkmapper.protoc.admin;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RectangleContainer.RectangleList;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;


public class RailInfoManager {
	private static FileList list;

	public static synchronized FileList getFileList() throws IOException {
		if (list == null)
			list = doGetFileList();
		return list;
	}

	private static FileList doGetFileList() throws IOException {
		ServerInfo info = ServerInfoManager.getServerInfo();
		URL url = new URL(info.getRailAddress() + "master.pbf");
		InputStream in = url.openStream();
		
		FileList l = FileList.parseFrom(in);
		return l;
	}

	private static void prepareFiles() throws IOException {
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrails");
		File[] filesToCopy = parent.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.startsWith("f_");
			}

		});
		int numInDirectory = 990;
		FileList.Builder fileListBuilder = FileList.newBuilder();

		File outParent = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/data");

		for (int i = 0; i < filesToCopy.length; i++) {
			int dc = i / numInDirectory;
			File directory = new File(outParent, "f_" + dc);
			directory.mkdirs();

			File src = filesToCopy[i];
			FileUtils.copyFile(src, new File(directory, src.getName()));
			FileInfo info = FileInfo.newBuilder().setFile(src.getName()).setParent(directory.getName() + "/").build();
			fileListBuilder.addFiles(info);
		}

		File outFile = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/master.pbf");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(fileListBuilder.build().toByteArray());
		out.close();
	}
	private static void checkPrepare() throws IOException {
		File inFile = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/master.pbf");
		FileInputStream in = new FileInputStream(inFile);
		byte[] data = new byte[(int) inFile.length()];
		in.read(data);
		in.close();
		
		FileList list = FileList.parseFrom(data);
		for (FileInfo f : list.getFilesList()) {
			File file = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/data/" + f.getParent() + "/" + f.getFile());
			if (!file.exists()) {
				System.out.println(file);
			}
		}
	}

	private static void writeToStagingDirectory() throws IOException {
		File parent = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/data");
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
		prepareFiles();
//		checkPrepare();
	}

}
