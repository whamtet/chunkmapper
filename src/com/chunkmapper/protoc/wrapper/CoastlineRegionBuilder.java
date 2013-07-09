package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineRegion;
import com.chunkmapper.protoc.CoastlineContainer.CoastlineRegion.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

public class CoastlineRegionBuilder implements RegionBuilder {
	public CoastlineRegion.Builder builder;
	public CoastlineRegionBuilder() {
		builder = CoastlineRegion.newBuilder();
	}
	public CoastlineRegionBuilder(Builder newBuilder) {
		builder = newBuilder;
	}
	
	public RegionBuilder addSection(SectionWrapper section) {
		CoastlineSectionWrapper section2 = (CoastlineSectionWrapper) section;
		builder.addCoastlineSections(section2.coastlineSection);
		return this;
	}
	public RegionWrapper build() {
		return new CoastlineRegionWrapper(builder.build());
	}
	public int getSectionCount() {
		return builder.getCoastlineSectionsCount();
	}
	public RegionWrapper getRegionWrapper(InputStream in) throws IOException {
		return new CoastlineRegionWrapper(CoastlineRegion.parseFrom(in));
	}
	public RegionWrapper getRegionWrapper(byte[] data) throws InvalidProtocolBufferException {
		return new CoastlineRegionWrapper(CoastlineRegion.parseFrom(data));
	}
}
