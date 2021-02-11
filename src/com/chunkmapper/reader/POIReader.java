package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.DataFormatException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.sections.POI;
import com.chunkmapper.writer.ArtifactWriter;

public class POIReader {
	private Collection<POI> pois;
	public static final ArrayList<SpecialPlace> specialPlaces = new ArrayList<SpecialPlace>();
	static {
		specialPlaces.add(new SpecialPlace(new String[] {"home"}, -27.5037, 153.0040));
		specialPlaces.add(new SpecialPlace(new String[] {"ma", "and", "pa"}, -39.0743, 174.0952));
		specialPlaces.add(new SpecialPlace(new String[] {"pete's", "house"}, -43.518, 172.583));
		specialPlaces.add(new SpecialPlace(new String[] {"dancing", "purple", "asteroids"}, -39.066, 174.046));
		specialPlaces.add(new SpecialPlace(new String[] {"I love", "you", "Tiantian"}, 22.2855, 114.0375));
	}
	public POIReader(Collection<POI> pois2, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		pois = pois2;
	}
	public void addSigns(Chunk chunk) {
		for (POI poi : pois) {
			int offsetx = poi.point.x - chunk.x0, offsetz = poi.point.z - chunk.z0;
			if (0 <= offsetx && offsetx < 16 && 0 <= offsetz && offsetz < 16) {
				int h = chunk.getHeights(offsetx, offsetz);
				if (h < 0) h = 4;
				if (poi.text != null)
					ArtifactWriter.addSign(chunk, h, offsetz, offsetx, poi.text.split(" "));
			}
		}
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
	public static class SpecialPlace {
		public final String[] text;
		public final int chunkx, chunkz, absx, absz;
		public final double[] latlon;
		public SpecialPlace(String[] text, double lat, double lon) {
			latlon = new double[] {lat, lon};
			this.text = text;
			chunkx = (int) Math.floor(lon * 3600 / 16);
			chunkz = (int) Math.floor(-lat * 3600 / 16);
			absx = (int) Math.floor(lon * 3600);
			absz = (int) Math.floor(-lat * 3600);
		}
		public String toString() {
			String t = text[0];
			for (int i = 1; i < text.length; i++) {
				t += " " + text[i];
			}
			return t;
		}
	}



}
