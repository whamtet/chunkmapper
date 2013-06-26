package com.chunkmapper.protoc.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.FileContainer.FileList;

public class CopyOver {
	private static void prepareFiles(String name) throws IOException, DataFormatException {
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/" + name);
		File[] filesToCopy = parent.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.startsWith("f_");
			}
		});
		int numInDirectory = 990;
		FileList.Builder fileListBuilder = FileList.newBuilder();

		File outParent = new File(String.format("/Users/matthewmolloy/workspace/chunkmapper_static/public/%s/data", name));

		for (int i = 0; i < filesToCopy.length; i++) {
			int dc = i / numInDirectory;
			File directory = new File(outParent, "f_" + dc);
			directory.mkdirs();

			File src = filesToCopy[i];
			ZipOver.zipOver(src, new File(directory, src.getName()));
			FileInfo info = FileInfo.newBuilder().setFile(src.getName()).setParent(directory.getName() + "/").build();
			fileListBuilder.addFiles(info);
		}

		File outFile = new File(String.format("/Users/matthewmolloy/workspace/chunkmapper_static/public/%s/master.pbf", name));
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
	
	private static void copyBack() throws IOException {
		File parentSrc = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrivers/data");
		File dest = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrivers");
		System.out.println(parentSrc.exists());
		dest.mkdirs();
		for (File folder : parentSrc.listFiles()) {
			if (folder.isDirectory()) {
				for (File src : folder.listFiles()) {
					if (src.getName().startsWith("f_")) {
						FileUtils.copyFile(src, new File(dest, src.getName()));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
//		copyBack();
		prepareFiles("myrails");
//		checkPrepare();
	}

}
