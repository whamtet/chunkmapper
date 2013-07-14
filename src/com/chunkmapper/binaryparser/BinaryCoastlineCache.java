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
import com.chunkmapper.protoc.CoastlineContainer;
import com.chunkmapper.protoc.CoastlineContainer.CoastlineRegion;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.sections.Coastline;

public class BinaryCoastlineCache {
	private final boolean offline;
	
	public BinaryCoastlineCache(boolean offline) {
		this.offline = offline;
	}
	
	public ArrayList<Coastline> getSections(FileInfo info2) throws IOException, URISyntaxException, DataFormatException {
		File CACHE = new File(Utila.CACHE, "mycoastlines");
		CACHE.mkdirs();
		File cacheFile = new File(CACHE, info2.getFile());
		boolean cacheValid = FileValidator.checkValid(cacheFile);
		
		byte[] data;
		if (offline) {
			File f = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/mycoastlines/data/" + info2.getParent() + info2.getFile());
			data = Zip.inflate(f);
		} else if (cacheValid) {
			data = Zip.inflate(cacheFile);
		} else {
			ServerInfo info = ServerInfoManager.getServerInfo();
			String address = info.getLakeAddress() + "data/" + info2.getParent() + info2.getFile();
			URL url = new URL(address);
			data = Zip.readFully(url.openStream());
			FileOutputStream out = new FileOutputStream(cacheFile);
			out.write(data);
			out.close();
			FileValidator.setValid(cacheFile);
			data = Zip.inflate(data);
		}
		
		CoastlineRegion coastlineRegion = CoastlineRegion.parseFrom(data);
		ArrayList<Coastline> out = new ArrayList<Coastline>();
		
		for (CoastlineContainer.CoastlineSection rawCoastline : coastlineRegion.getCoastlineSectionsList()) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point rootPoint = null;
			for (PointContainer.Point rawPoint : rawCoastline.getPointsList()) {
				if (rootPoint == null) {
					rootPoint = new Point(rawPoint.getX(), rawPoint.getZ());
					points.add(rootPoint);
				} else {
					points.add(new Point(rawPoint.getX() + rootPoint.x, rawPoint.getZ() + rootPoint.z));
				}
			}
			RectangleContainer.Rectangle r = rawCoastline.getBbox();
			Rectangle bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
			out.add(new Coastline(points, bbox));
		}

		return out;
	}

}
