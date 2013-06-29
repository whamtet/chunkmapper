package com.chunkmapper.protoc.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.RiverContainer.RiverSection;


public class RiverRegionWrapper implements RegionWrapper {

	public RiverRegion riverRegion;
	public RiverRegionWrapper(RiverRegion build) {
		riverRegion = build;
	}

	@Override
	public byte[] toByteArray() {
		return riverRegion.toByteArray();
	}

	@Override
	public List<SectionWrapper> getSectionsList() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (RiverSection riverSection : riverRegion.getRiverSectionsList()) {
			out.add(new RiverSectionWrapper(riverSection));
		}
		return out;
	}

	@Override
	public RegionBuilder newBuilder() {
		return new RiverRegionBuilder();
	}

}
