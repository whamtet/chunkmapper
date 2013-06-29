package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;

import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.RiverContainer.RiverSection;
import com.google.protobuf.InvalidProtocolBufferException;

public class RiverSectionWrapper implements SectionWrapper {

	public RiverSection riverSection;
	public RiverSectionWrapper(RiverSection build) {
		riverSection = build;
	}

	@Override
	public Rectangle getBbox() {
		RectangleContainer.Rectangle r = riverSection.getBbox();
		return new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
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
