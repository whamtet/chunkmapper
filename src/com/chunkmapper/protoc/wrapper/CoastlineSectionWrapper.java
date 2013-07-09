package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.CoastlineContainer.CoastlineRegion;
import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class CoastlineSectionWrapper implements SectionWrapper {
	public final CoastlineSection coastlineSection;
	public final ArrayList<Point> points = new ArrayList<Point>();
	private final Rectangle bbox;
	public CoastlineSectionWrapper(CoastlineSection coastlineSection2) {
		coastlineSection = coastlineSection2;
		RectangleContainer.Rectangle bbox2 = coastlineSection.getBbox();
		bbox = new Rectangle(bbox2.getX(), bbox2.getZ(), bbox2.getWidth(), bbox2.getHeight());
		for (PointContainer.Point rawPoint : coastlineSection2.getPointsList()) {
			points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
		}
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof CoastlineSectionWrapper))
			return false;
		CoastlineSectionWrapper other2 = (CoastlineSectionWrapper) other;
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
		return new CoastlineRegionBuilder();
	}

	@Override
	public RegionBuilder newRegionBuilder(byte[] data)
			throws InvalidProtocolBufferException {
		return new CoastlineRegionBuilder(CoastlineRegion.newBuilder(CoastlineRegion.parseFrom(data)));
	}

}
