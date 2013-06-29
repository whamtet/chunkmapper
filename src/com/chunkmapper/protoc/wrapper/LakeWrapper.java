package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;

import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.LakeContainer.LakeRegion;
import com.chunkmapper.protoc.RectangleContainer;
import com.google.protobuf.InvalidProtocolBufferException;

public class LakeWrapper implements SectionWrapper {

	public final Lake lake;
	public LakeWrapper(Lake l) {
		lake = l;
	}

	@Override
	public Rectangle getBbox() {
		RectangleContainer.Rectangle r = lake.getBbox();
		return new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
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
