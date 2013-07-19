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
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.sections.RailSection;

public class BinaryRailCache {
	private boolean offline;
	public BinaryRailCache(boolean offline) {
		this.offline = offline;
	}
	
	public ArrayList<RailSection> getSections(FileInfo info2) throws IOException, URISyntaxException, DataFormatException {
		File CACHE = new File(Utila.CACHE, "myrails");
		File cacheFile = new File(CACHE, info2.getFile());
		boolean cacheValid = FileValidator.checkValid(cacheFile);
		
		byte[] data;
		if (offline) {
			File f = new File("/Users/matthewmolloy/workspace/chunkmapper_static/public/myrails/data/" + info2.getParent() + info2.getFile());
			data = Zip.inflate(f);
		} else if (cacheValid) {
			data = Zip.inflate(cacheFile);
		} else {
			ServerInfo info = ServerInfoManager.getServerInfo();
			String address = info.getRailAddress() + "data/" + info2.getParent() + info2.getFile();
			URL url = new URL(address);
			data = Zip.readFully(url.openStream());
			FileOutputStream out = new FileOutputStream(cacheFile);
			out.write(data);
			out.close();
			FileValidator.setValid(cacheFile);
			data = Zip.inflate(data);
		}
		
		RailRegion railRegion = RailRegion.parseFrom(data);
		ArrayList<RailSection> out = new ArrayList<RailSection>();
		
		for (RailSectionContainer.RailSection rawRailSection : railRegion.getRailSectionsList()) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point rootPoint = null;
			for (PointContainer.Point rawPoint : rawRailSection.getPointsList()) {
				if (rootPoint == null) {
					rootPoint = new Point(rawPoint.getX(), rawPoint.getZ());
					points.add(rootPoint);
				} else {
					points.add(new Point(rootPoint.x + rawPoint.getX(), rootPoint.z + rawPoint.getZ()));
				}
			}
			RectangleContainer.Rectangle rawBbox = rawRailSection.getBbox();
			Rectangle bbox = new Rectangle(rawBbox.getX(), rawBbox.getZ(), rawBbox.getWidth(), rawBbox.getHeight());
			out.add(new RailSection(points, rawRailSection.getIsPreserved(), rawRailSection.getHasBridge(),
					rawRailSection.getHasCutting(), rawRailSection.getHasEmbankment(), rawRailSection.getHasTunnel(),
					bbox)); 
		}
		return out;
	}

}
