package com.chunkmapper.protoc.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface RegionBuilder {

	public RegionBuilder newBuilder();

	public List<SectionWrapper> getSections();

	public void addSection(SectionWrapper section);

	public int getSectionCount();

	public RegionBuilder newBuilder(InputStream in) throws IOException;

	public byte[] toByteArray() throws IOException;

}
