package com.chunkmapper.protoc;

import java.io.IOException;
import java.io.InputStream;

public interface RegionBuilder {

	public RegionBuilder addSection(SectionWrapper section);

	public RegionWrapper build();

	public int getSectionCount();

	public RegionWrapper getRegionWrapper(InputStream in) throws IOException;

}