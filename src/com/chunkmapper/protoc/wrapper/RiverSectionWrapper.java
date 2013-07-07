package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.RiverContainer.RiverSection;
import com.google.protobuf.InvalidProtocolBufferException;

public class RiverSectionWrapper implements SectionWrapper {

	public RiverSection riverSection;
	private final Rectangle bbox;
	public final ArrayList<Point> points = new ArrayList<Point>();
	public RiverSectionWrapper(RiverSection build) {
		riverSection = build;
		RectangleContainer.Rectangle r = build.getBbox();
		bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
		for (PointContainer.Point rawPoint : build.getPointsList()) {
			points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
		}
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof RiverSectionWrapper))
			return false;
		RiverSectionWrapper other2 = (RiverSectionWrapper) other;
		if (other2.points.size() != points.size()) {
			return false;
		}
		for (int i = 0; i < points.size(); i++) {
			if (!points.get(i).equals(other2.points.get(i))) {
				return false;
			}
		}
		return true;
	}
	public int hashCode() {
		return bbox.hashCode();
	}

	@Override
	public Rectangle getBbox() {
		return bbox;
	}

	@Override
	public RegionBuilder newRegionBuilder() {
		return new RiverRegionBuilder();
	}

	@Override
	public RegionBuilder newRegionBuilder(byte[] data) throws InvalidProtocolBufferException {
		return new RiverRegionBuilder(RiverRegion.newBuilder(RiverRegion.parseFrom(data)));
	}

}
