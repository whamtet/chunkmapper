package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.Utila;
import com.chunkmapper.Zip;
import com.chunkmapper.downloader.SynchronousDownloader;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.LakeContainer;
import com.chunkmapper.protoc.LakeContainer.LakeRegion;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.sections.Lake;

public class BinaryLakeCache {
	private SynchronousDownloader downloader = new SynchronousDownloader();
	private final boolean offline;
	
	public BinaryLakeCache(boolean offline) {
		this.offline = offline;
	}
	
	public ArrayList<Lake> getSections(FileInfo info2) throws IOException, URISyntaxException, DataFormatException {
		File CACHE = new File(Utila.CACHE, "mylakes");
		CACHE.mkdirs();
		File cacheFile = new File(CACHE, info2.getFile());
		
		InputStream in;
		if (offline) {
			File f = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/mylakes/data/" + info2.getParent() + info2.getFile());
			in = new FileInputStream(f);
		} else if (FileValidator.checkValid(cacheFile)) {
			in = new FileInputStream(cacheFile);
		} else {
			ServerInfo info = ServerInfoManager.getServerInfo();
			String address = info.getRailAddress() + "data/" + info2.getParent() + info2.getFile();
			URL url = new URL(address);
			in = url.openStream();
		}
		
		byte[] data = Zip.inflate(in);
		LakeRegion lakeRegion = LakeRegion.parseFrom(data);
		ArrayList<Lake> out = new ArrayList<Lake>();
		
		for (LakeContainer.Lake rawLake : lakeRegion.getLakesList()) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point rootPoint = null;
			for (PointContainer.Point rawPoint : rawLake.getPointsList()) {
				if (rootPoint == null) {
					rootPoint = new Point(rawPoint.getX(), rawPoint.getZ());
					points.add(rootPoint);
				} else {
					points.add(new Point(rawPoint.getX() + rootPoint.x, rawPoint.getZ() + rootPoint.z));
				}
			}
			RectangleContainer.Rectangle r = rawLake.getBbox();
			Rectangle bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
			out.add(new Lake(points, bbox, rawLake.getIsInner(), rawLake.getIsCove(), rawLake.getIsLagoon()));
		}

		return out;
	}

	public void shutdown() {
		downloader.shutdown();
		
	}

}
