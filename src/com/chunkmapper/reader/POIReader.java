package com.chunkmapper.reader;

import java.io.IOException;
import java.util.HashSet;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.sections.POI;
import com.chunkmapper.writer.ArtifactWriter;

public class POIReader {
	private HashSet<POI> pois;
	public POIReader(int regionx, int regionz) throws IOException {
		pois = POIParser.getPois(regionx, regionz);
	}
	public void addSigns(Chunk chunk) {
		for (POI poi : pois) {
			int offsetx = poi.point.x - chunk.x0, offsetz = poi.point.z - chunk.z0;
			if (0 <= offsetx && offsetx < 16 && 0 <= offsetz && offsetz < 16) {
				int h = chunk.getHeights(offsetx, offsetz);
				if (h < 0) h = 4;
				ArtifactWriter.addSign(chunk, h, offsetz, offsetx, poi.text.split(" "));
			}
		}
	}

}
