package com.chunkmapper.protoc;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.RailSectionContainer.RailSection;

public interface RegionWrapper {

	public byte[] toByteArray();

	public List<SectionWrapper> getSectionsList();

	public RegionBuilder newBuilder();

}