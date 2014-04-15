package com.chunkmapper.reader;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.DataFormatException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.sections.POI;

public class RugbyReader {
	private final Collection<POI> pois;
	public RugbyReader(Collection<POI> pois, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		this.pois = pois;
	}
	public static class RugbyField {
		public final String name;
		public RugbyField(String name) {
			this.name = name;
		}
	}
	public RugbyField getRugbyField(Chunk chunk) {
		for (POI poi : pois) {
			boolean poiIsRugby = "rugby".equals(poi.type) || "rugby_league".equals(poi.type)
					|| "rugby_union".equals(poi.type);
			if (poiIsRugby && chunk.x0 <= poi.point.x && poi.point.x < chunk.x0 + 16
					&& chunk.z0 <= poi.point.z && poi.point.z < chunk.z0 + 16) {
				return new RugbyField(poi.text);
			}
		}
		return null;
	}

}
