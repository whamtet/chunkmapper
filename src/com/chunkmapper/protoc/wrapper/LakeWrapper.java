package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.LakeContainer.LakeRegion;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class LakeWrapper implements SectionWrapper {

	public final Lake lake;
	private final Rectangle bbox;
	public final ArrayList<Point> points = new ArrayList<Point>();
	public LakeWrapper(Lake l) {
		lake = l;
		RectangleContainer.Rectangle r = l.getBbox();
		bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
		for (PointContainer.Point rawPoint : l.getPointsList()) {
			points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
		}
	}
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof LakeWrapper))
			return false;
		LakeWrapper other2 = (LakeWrapper) other;
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
		return new LakeRegionBuilder();
	}

	@Override
	public RegionBuilder newRegionBuilder(byte[] data)
			throws InvalidProtocolBufferException {
		return new LakeRegionBuilder(LakeRegion.newBuilder(LakeRegion.parseFrom(data)));
	}

}
