package com.chunkmapper.protoc.wrapper;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.POIContainer.POI;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;

public class POIWrapper implements SectionWrapper {
	public final POI poi;
	public final Rectangle bbox;
	public final Point point;
	
	public POIWrapper(POI poi) {
		this.poi = poi;
		PointContainer.Point p = poi.getPoint();
		bbox = new Rectangle(p.getX(), p.getZ(), 1, 1);
		point = new Point(p.getX(), p.getZ());
	}
	@Override
	public Rectangle getBbox() {
		return bbox;
	}

	@Override
	public RegionBuilder getRegion(InputStream in) throws IOException {
		RegionBuilder out = new POIRegionBuilder();
		DataInputStream in2 = new DataInputStream(in);
		try {
			while (true) {
				int size = in2.readInt();
				byte[] data = new byte[size];
				in2.readFully(data);
				out.addSection(new POIWrapper(POI.parseFrom(data)));
			}
		} catch (EOFException e) {
		}
		return out;
	}

	@Override
	public byte[] toByteArray() {
		return poi.toByteArray();
	}
	
	@Override
	public int hashCode() {
		return bbox.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof POIWrapper))
			return false;
		POIWrapper other2 = (POIWrapper) other;
		return other2.point.equals(point);
	}
//		
	

}
