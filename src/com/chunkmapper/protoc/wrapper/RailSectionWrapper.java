package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer.RailSection;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class RailSectionWrapper implements SectionWrapper {

	public RailSection railSection;
	private final Rectangle bbox;
	public final ArrayList<Point> points = new ArrayList<Point>();
	public RailSectionWrapper(RailSection section) {
		railSection = section;
		RectangleContainer.Rectangle r = railSection.getBbox();
		bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
		for (PointContainer.Point rawPoint : section.getPointsList()) {
			points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
		}
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof RailSectionWrapper))
			return false;
		RailSectionWrapper other2 = (RailSectionWrapper) other;
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
		return new RailRegionBuilder();
	}

	@Override
	public RegionBuilder newRegionBuilder(byte[] data)
			throws InvalidProtocolBufferException {
		return new RailRegionBuilder(RailRegion.newBuilder(RailRegion.parseFrom(data)));
	}

}
