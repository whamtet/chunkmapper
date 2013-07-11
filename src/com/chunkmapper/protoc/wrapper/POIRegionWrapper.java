package com.chunkmapper.protoc.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;
import com.chunkmapper.protoc.POIContainer.POI;
import com.chunkmapper.protoc.POIContainer.POIRegion;

public class POIRegionWrapper implements RegionWrapper {
	public final POIRegion poiRegion;

	public POIRegionWrapper(POIRegion poiRegion) {
		this.poiRegion = poiRegion;
	}

	@Override
	public byte[] toByteArray() {
		return poiRegion.toByteArray();
	}

	@Override
	public List<SectionWrapper> getSectionsList() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (POI poi : poiRegion.getPoisList()) {
			out.add(new POIWrapper(poi));
		}
		return out;
	}

	@Override
	public RegionBuilder newBuilder() {
		return new POIRegionBuilder();
	}
}
