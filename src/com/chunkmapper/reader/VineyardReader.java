package com.chunkmapper.reader;

import java.io.IOException;

import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassParser;
import com.chunkmapper.parser.OverpassParser.OverpassObject;

public class VineyardReader {

	public VineyardReader(int regionx, int regionz) throws IOException {
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("motueka, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
	}

}
