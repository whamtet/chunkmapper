package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.chunkmapper.protoc.POIContainer.POIRegion;
import com.google.protobuf.InvalidProtocolBufferException;

public class POIRegionBuilder implements RegionBuilder {
	public POIRegion.Builder builder;

	public POIRegionBuilder() {
		builder = POIRegion.newBuilder();
	}
	public POIRegionBuilder(POIRegion.Builder newBuilder) {
		builder = newBuilder;
	}
	
	public RegionBuilder addSection(SectionWrapper section) {
		POIWrapper poi = (POIWrapper) section;
		builder.addPois(poi.poi);
		return this;
	}
	public RegionWrapper build() {
		return new POIRegionWrapper(builder.build());
//		return new CoastlineRegionWrapper(builder.build());
	}
	public int getSectionCount() {
		return builder.getPoisCount();
	}
	public RegionWrapper getRegionWrapper(InputStream in) throws IOException {
		return new POIRegionWrapper(POIRegion.parseFrom(in));
	}
	public RegionWrapper getRegionWrapper(byte[] data) throws InvalidProtocolBufferException {
		return new POIRegionWrapper(POIRegion.parseFrom(data));
	}
}
