package com.chunkmapper.protoc.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.LakeContainer.LakeRegion;

public class LakeRegionWrapper implements RegionWrapper {
	public final LakeRegion lakeRegion;
	public LakeRegionWrapper(LakeRegion region) {
		lakeRegion = region;
	}

	@Override
	public byte[] toByteArray() {
		return lakeRegion.toByteArray();
	}

	@Override
	public List<SectionWrapper> getSectionsList() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (Lake lake : lakeRegion.getLakesList()) {
			out.add(new LakeWrapper(lake));
		}
		return out;
	}

	@Override
	public RegionBuilder newBuilder() {
		return new LakeRegionBuilder();
	}

}
