package com.chunkmapper.writer;

import java.util.ArrayList;

import com.chunkmapper.chunk.Chunk;

public class SpecialLandmarksWriter {
	private static final ArrayList<SpecialPlace> specialPlaces = new ArrayList<SpecialPlace>();
	static {
		specialPlaces.add(new SpecialPlace(new String[] {"home"}, -27.5037, 153.0040));
		specialPlaces.add(new SpecialPlace(new String[] {"ma", "and", "pa"}, -39.0743, 174.0952));
	}
	
	public static void addSpecialLandmarks(Chunk chunk) {
		for (SpecialPlace specialPlace : specialPlaces) {
			if (chunk.abschunkx == specialPlace.chunkx && chunk.abschunkz == specialPlace.chunkz) {
				int relx = specialPlace.absx - chunk.x0;
				int relz = specialPlace.absz - chunk.z0;
				ArtifactWriter.addSign(chunk, chunk.getHeights(relx, relz), relz, relx, specialPlace.text);
			}
		}
		
	}
	private static class SpecialPlace {
		public final String[] text;
		public final int chunkx, chunkz, absx, absz;
		public SpecialPlace(String[] text, double lat, double lon) {
			this.text = text;
			chunkx = (int) Math.floor(lon * 3600 / 16);
			chunkz = (int) Math.floor(-lat * 3600 / 16);
			absx = (int) Math.floor(lon * 3600);
			absz = (int) Math.floor(-lat * 3600);
		}
	}

}
