package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;

import com.chunkmapper.protoc.LakeContainer.LakeRegion;
import com.chunkmapper.protoc.LakeContainer.LakeRegion.Builder;
import com.chunkmapper.protoc.RegionBuilder;
import com.chunkmapper.protoc.RegionWrapper;
import com.chunkmapper.protoc.SectionWrapper;

public class LakeRegionBuilder implements RegionBuilder {
	public LakeRegion.Builder builder;
	public LakeRegionBuilder() {
		builder = LakeRegion.newBuilder();
	}

	public LakeRegionBuilder(Builder newBuilder) {
		builder = newBuilder;
	}

	@Override
	public RegionBuilder addSection(SectionWrapper section) {
		LakeWrapper section2 = (LakeWrapper) section;
		builder.addLakes(section2.lake);
		return this;
	}

	@Override
	public RegionWrapper build() {
		return new LakeRegionWrapper(builder.build());
	}

	@Override
	public int getSectionCount() {
		return builder.getLakesCount();
	}

	@Override
	public RegionWrapper getRegionWrapper(InputStream in) throws IOException {
		return new LakeRegionWrapper(LakeRegion.parseFrom(in));
	}

}
