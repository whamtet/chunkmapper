package com.chunkmapper.protoc.wrapper;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;

public interface SectionWrapper {

	public Rectangle getBbox();

	public RegionBuilder getRegion(InputStream in) throws IOException;

	public byte[] toByteArray();

}
