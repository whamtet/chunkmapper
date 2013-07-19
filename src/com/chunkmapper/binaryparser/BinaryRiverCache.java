package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.Utila;
import com.chunkmapper.Zip;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RiverContainer;
import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.sections.RiverSection;

public class BinaryRiverCache {
	private final boolean offline;
	public BinaryRiverCache(boolean offline) {
		this.offline = offline;
	}
	
	public ArrayList<RiverSection> getSections(FileInfo info2) throws IOException, URISyntaxException, DataFormatException {
		File CACHE = new File(Utila.CACHE, "myrails");
		CACHE.mkdirs();
		File cacheFile = new File(CACHE, info2.getFile());
		boolean cacheValid = FileValidator.checkValid(cacheFile);
		
		byte[] data;
		if (offline) {
			File f = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrivers/data/" + info2.getParent() + info2.getFile());
			data = Zip.inflate(f);
		} else if (cacheValid) {
			data = Zip.inflate(cacheFile);
		} else {
			ServerInfo info = ServerInfoManager.getServerInfo();
			String address = info.getRiverAddress() + "data/" + info2.getParent() + info2.getFile();
			URL url = new URL(address);
			data = Zip.readFully(url.openStream());
			FileOutputStream out = new FileOutputStream(cacheFile);
			out.write(data);
			out.close();
			FileValidator.setValid(cacheFile);
			data = Zip.inflate(data);
		}
		
		RiverRegion riverRegion = RiverRegion.parseFrom(data);
		ArrayList<RiverSection> out = new ArrayList<RiverSection>();
		
		for (RiverContainer.RiverSection rawRiverSection : riverRegion.getRiverSectionsList()) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point rootPoint = null;
			for (PointContainer.Point rawPoint : rawRiverSection.getPointsList()) {
				if (rootPoint == null) {
					rootPoint = new Point(rawPoint.getX(), rawPoint.getZ());
					points.add(rootPoint);
				} else {
					points.add(new Point(rawPoint.getX() + rootPoint.x, rawPoint.getZ() + rootPoint.z));
				}
			}
			RectangleContainer.Rectangle r = rawRiverSection.getBbox();
			Rectangle bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
			out.add(new RiverSection(points, bbox));
		}

		return out;
	}


}
