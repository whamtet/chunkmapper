package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.POIContainer.POI;
import com.chunkmapper.protoc.POIContainer.POIRegion;
import com.chunkmapper.protoc.PointContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class POIWrapper implements SectionWrapper {
	public final POI poi;
	public final Point point;
	public final Rectangle bbox;
	public POIWrapper(POI poi) {
		this.poi = poi;
		PointContainer.Point rawPoint = poi.getPoint();
		this.point = new Point(rawPoint.getX(), rawPoint.getZ());
		bbox = new Rectangle(rawPoint.getX(), rawPoint.getZ(), 0, 0);
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof POIWrapper))
			return false;
		POIWrapper other2 = (POIWrapper) other;
		return other2.point.equals(point);
	}
	public int hashCode() {
		return point.hashCode();
	}

	@Override
	public Rectangle getBbox() {
		return bbox;
	}

	@Override
	public RegionBuilder newRegionBuilder() {
		return new POIRegionBuilder();
	}

	@Override
	public RegionBuilder newRegionBuilder(byte[] data)
			throws InvalidProtocolBufferException {
		return new POIRegionBuilder(POIRegion.newBuilder(POIRegion.parseFrom(data)));
	}

}
