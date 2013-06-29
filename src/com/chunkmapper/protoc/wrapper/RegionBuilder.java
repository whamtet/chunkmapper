package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.InvalidProtocolBufferException;


public interface RegionBuilder {

	public RegionBuilder addSection(SectionWrapper section);

	public RegionWrapper build();

	public int getSectionCount();

	public RegionWrapper getRegionWrapper(InputStream in) throws IOException;

	public RegionWrapper getRegionWrapper(byte[] data) throws InvalidProtocolBufferException;

}