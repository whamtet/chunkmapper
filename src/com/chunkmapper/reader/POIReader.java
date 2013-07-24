package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.downloader.OSMDownloader;
import com.chunkmapper.enumeration.OSMSource;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.sections.POI;
import com.chunkmapper.writer.ArtifactWriter;

public class POIReader {
	private Collection<POI> pois;
	private static final ArrayList<SpecialPlace> specialPlaces = new ArrayList<SpecialPlace>();
	static {
		specialPlaces.add(new SpecialPlace(new String[] {"home"}, -27.5037, 153.0040));
		specialPlaces.add(new SpecialPlace(new String[] {"ma", "and", "pa"}, -39.0743, 174.0952));
		specialPlaces.add(new SpecialPlace(new String[] {"pete's", "house"}, -43.518, 172.583));
		specialPlaces.add(new SpecialPlace(new String[] {"dancing", "purple", "asteroids"}, -39.066, 174.046));
	}
	public POIReader(int regionx, int regionz) throws IOException, URISyntaxException {
		pois = (Collection<POI>) OSMDownloader.getSections(OSMSource.poi, regionx, regionz);
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
