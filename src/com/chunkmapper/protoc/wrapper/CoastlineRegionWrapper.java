package com.chunkmapper.protoc.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineRegion;
import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;

public class CoastlineRegionWrapper implements RegionWrapper {
	public final CoastlineRegion coastlineRegion;

	public CoastlineRegionWrapper(CoastlineRegion build) {
		coastlineRegion = build;
	}

	@Override
	public byte[] toByteArray() {
		return coastlineRegion.toByteArray();
	}

	@Override
	public List<SectionWrapper> getSectionsList() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (CoastlineSection coastlineSection : coastlineRegion.getCoastlineSectionsList()) {
			out.add(new CoastlineSectionWrapper(coastlineSection));
		}
		return out;
	}

	@Override
	public RegionBuilder newBuilder() {
		return new CoastlineRegionBuilder();
	}
}
