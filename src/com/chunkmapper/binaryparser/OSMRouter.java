package com.chunkmapper.binaryparser;

import java.io.IOException;
import java.util.zip.DataFormatException;

import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassParser;

public class OSMRouter {
	/*
	 * Simple Wrapper to direct requests for OverpassObject depending on config setting.
	 */
	
	private static boolean isLiveMode;
	public static OverpassObject getObject(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		if (isLiveMode) {
			return (new OverpassParser()).getObject(regionx, regionz);
		} else {
			return (new OsmosisParser()).getObject(regionx, regionz);
		}
	}
	public static void setLive() {
		isLiveMode = true;
	}

}
