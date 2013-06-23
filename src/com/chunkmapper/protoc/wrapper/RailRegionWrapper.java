package com.chunkmapper.protoc.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer.RailSection;
import com.chunkmapper.protoc.RegionBuilder;
import com.chunkmapper.protoc.RegionWrapper;
import com.chunkmapper.protoc.SectionWrapper;


public class RailRegionWrapper implements RegionWrapper {

	public RailRegion railRegion;
	public RailRegionWrapper(RailRegion build) {
		railRegion = build;
	}

	@Override
	public byte[] toByteArray() {
		return railRegion.toByteArray();
	}

	@Override
	public List<SectionWrapper> getSectionsList() {
		List<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (RailSection section : railRegion.getRailSectionsList()) {
			out.add(new RailSectionWrapper(section));
		}
		return out;
	}

	@Override
	public RegionBuilder newBuilder() {
		return new RailRegionBuilder();
	}

}
