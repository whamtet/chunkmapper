package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion.Builder;
import com.chunkmapper.protoc.RegionBuilder;
import com.chunkmapper.protoc.RegionWrapper;
import com.chunkmapper.protoc.SectionWrapper;



public class RailRegionBuilder implements RegionBuilder {

	public Builder builder;
	
	public RailRegionBuilder(Builder builder) {
		this.builder = builder;
	}
	public RailRegionBuilder() {
		this.builder = RailRegion.newBuilder();
	}

	@Override
	public RegionBuilder addSection(SectionWrapper section) {
		RailSectionWrapper section2 = (RailSectionWrapper) section;
		builder.addRailSections(section2.railSection);
		return this;
	}

	@Override
	public RegionWrapper build() {
		return new RailRegionWrapper(builder.build());
	}

	@Override
	public int getSectionCount() {
		return builder.getRailSectionsCount();
	}

	@Override
	public RegionWrapper getRegionWrapper(InputStream in) throws IOException {
		return new RailRegionWrapper(RailRegion.parseFrom(in));
	}

}
