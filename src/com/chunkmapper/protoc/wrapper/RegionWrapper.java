package com.chunkmapper.protoc.wrapper;

import java.util.List;

public interface RegionWrapper {

	public byte[] toByteArray();

	public List<SectionWrapper> getSectionsList();

	public RegionBuilder newBuilder();

}