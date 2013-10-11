package com.chunkmapper.admin;

import java.io.IOException;
import java.util.zip.DataFormatException;

import com.chunkmapper.binaryparser.OsmosisParser;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassParser;

public class OSMRouter {
	private static boolean isLiveMode;
	public static OverpassObject getObject(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		if (isLiveMode) {
			return OverpassParser.getObject(regionx, regionz);
		} else {
			return OsmosisParser.getObject(regionx, regionz);
		}
	}
	public static void setLive() {
		isLiveMode = true;
	}

}
