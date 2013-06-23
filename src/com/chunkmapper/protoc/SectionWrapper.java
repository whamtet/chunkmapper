package com.chunkmapper.protoc;

import com.chunkmapper.protoc.RailSectionContainer.RailSection;
import com.google.protobuf.InvalidProtocolBufferException;

public interface SectionWrapper {


	public java.awt.Rectangle getBbox();

	public RegionBuilder newRegionBuilder();

	public RegionBuilder newRegionBuilder(byte[] data) throws InvalidProtocolBufferException;

}