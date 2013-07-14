package com.chunkmapper.downloader.binary;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.downloader.Downloader;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.protoc.wrapper.RegionBuilder;
import com.chunkmapper.protoc.wrapper.RegionWrapper;
import com.chunkmapper.protoc.wrapper.SectionWrapper;

public abstract class BinaryDownloader extends Downloader {

	protected void doDownload(Point p, FileList fileList, String rootURL, 
			RegionBuilder regionBuilder, File parentFile) throws IOException, URISyntaxException {
		
		Rectangle myRectangle = new Rectangle(p.x * 512, p.z * 512, 512, 512);
		for (FileInfo info : fileList.getFilesList()) {
			Rectangle fileRectangle = getRectangle(info);
			if (myRectangle.intersects(fileRectangle)) {
				ConnectionWrapper wrapper = super.getInputStream(rootURL + info.getParent() + info.getFile());
				RegionWrapper regionWrapper = regionBuilder.getRegionWrapper(wrapper.in);
				wrapper.closeInputStream();
				for (SectionWrapper section : regionWrapper.getSectionsList()) {
					if (section.getBbox().intersects(myRectangle)) {
						regionBuilder.addSection(section);
					}
				}
			}
		}
		File outputFile = new File(parentFile, "f_" + p.x + "_" + p.z);
		FileOutputStream out = new FileOutputStream(outputFile);
		out.write(regionBuilder.build().toByteArray());
		out.close();
		FileValidator.setValid(outputFile);
	}

	private static Rectangle getRectangle(FileInfo info) {
		// TODO Auto-generated method stub
		String[] split = info.getFile().split("_");
		int x = Integer.parseInt(split[1]);
		int z = Integer.parseInt(split[2]);
		int width = Integer.parseInt(split[3]);
		int height = Integer.parseInt(split[4]);
		return new Rectangle(x, z, width, height);
	}

	private static URL getURL(String rootURL, FileInfo info) {
		// TODO Auto-generated method stub
		try {
			return new URL(rootURL + info.getParent() + info.getFile());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		BinaryRailsDownloader downloader = new BinaryRailsDownloader();
		downloader.addTask(2, 2);
	}

	
}
