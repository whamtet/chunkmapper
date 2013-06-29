package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;

import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer.RailSection;
import com.chunkmapper.protoc.RectangleContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class RailSectionWrapper implements SectionWrapper {

	public RailSection railSection;
	public RailSectionWrapper(RailSection section) {
		railSection = section;
	}

	@Override
	public Rectangle getBbox() {
		RectangleContainer.Rectangle r =  railSection.getBbox();
		return new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
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
