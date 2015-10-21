package com.chunkmapper.binaryparser;

import java.io.IOException;
import java.util.zip.DataFormatException;

import com.chunkmapper.parser.OverpassObject;

public interface OverpassObjectSource {

	/*
	 * Currently two implementations: OsmosisParser and OverpassParser.
	 * Both of them get Open Streetmap Data.
	 * OsmosisParser reads off the live Open Stret Map server (slow).
	 * OverpassParser reads data archived on Amazon S3 (default).
	 */
	
	/*
	 * Get Open Street Map data at point (regionX, regionZ).
	 */
	public OverpassObject getObject(int regionx, int regionz)
			throws IOException, InterruptedException, DataFormatException;

}