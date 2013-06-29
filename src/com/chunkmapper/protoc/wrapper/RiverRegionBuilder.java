package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.RiverContainer.RiverRegion.Builder;
import com.google.protobuf.InvalidProtocolBufferException;


public class RiverRegionBuilder implements RegionBuilder {

	public Builder builder;
	
	public RiverRegionBuilder() {
		builder = RiverRegion.newBuilder();
	}
	public RiverRegionBuilder(Builder builder) {
		this.builder = builder;
	}

	@Override
	public RegionBuilder addSection(SectionWrapper section) {
		RiverSectionWrapper section2 = (RiverSectionWrapper) section;
		builder.addRiverSections(section2.riverSection);
		return this;
	}

	@Override
	public RegionWrapper build() {
		return new RiverRegionWrapper(builder.build());
	}

	@Override
	public int getSectionCount() {
		return builder.getRiverSectionsCount();
	}

	@Override
	public RegionWrapper getRegionWrapper(InputStream in) throws IOException {
		return new RiverRegionWrapper(RiverRegion.parseFrom(in));
	}
	@Override
	public RegionWrapper getRegionWrapper(byte[] data)
			throws InvalidProtocolBufferException {
		return new RiverRegionWrapper(RiverRegion.parseFrom(data));
	}

}
