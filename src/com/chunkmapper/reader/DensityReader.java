package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import com.chunkmapper.downloader.OSMDownloader;
import com.chunkmapper.enumeration.OSMSource;
import com.chunkmapper.parser.OverpassParser;
import com.chunkmapper.parser.OverpassParser.Node;
import com.chunkmapper.parser.OverpassParser.OverpassObject;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.sections.POI;
import com.chunkmapper.sections.Section;

public class DensityReader {
	public final float[][] data = new float[32][32];
	private final Random random = new Random();
	public DensityReader(int regionx0, int regionz0) throws IOException, URISyntaxException {
		//loop through neighbours
		for (int regionx = regionx0 - 1; regionx <= regionx0 + 1; regionx++) {
			for (int regionz = regionz0 - 1; regionz <= regionz0 + 1; regionz++) {
				//for each neighbour, get points of interest
				
				for (POI poi : POIParser.getPois(regionx, regionz)) {
					if (poi.population != null && (poi.type.equals("city")
							|| poi.type.equals("town") || poi.type.equals("village") || poi.type.equals("hamlet")
							|| poi.type.equals("suburb") || poi.type.equals("neighbourhood")
							|| poi.type.equals("isolated_dwelling"))) {
						double sigma = Math.sqrt(poi.population) * .4; //radius
						for (int i = 0; i < 32; i++) {
							for (int j = 0; j < 32; j++) {
								int absx = regionx0 * 512 + j*16, absz = regionz0 * 512 + i*16;
								int dx = absx - poi.point.x, dz = absz - poi.point.z;
								double dist = Math.sqrt(dx*dx + dz*dz);
								double factor = dist / sigma;
								data[i][j] += Math.exp(-factor*factor/2);
							}
						}
//						double factor = 3e5;
//						double density = Math.sqrt(poi.population) / factor;
//						double maxRad = Math.sqrt(3 * factor * Math.sqrt(poi.population) / Math.PI);
//						for (int i = 0; i < 512; i++) {
//							for (int j = 0; j < 512; j++) {
//								int absx = regionx0 * 512 + j, absz = regionz0 * 512 + i;
//								int dx = absx - poi.point.x, dz = absz - poi.point.z;
//								double dist = Math.sqrt(dx*dx + dz*dz) * 31;
//								if (dist < maxRad) {
//									data[i][j] += density * (1 - dist / maxRad);
//								}
//							}
//						}
					}
				}
			}
		}
	}
	public boolean hasHouse(int chunkx, int chunkz) {
//		return random.nextDouble() < 1e6 * data[chunkz*16][chunkx*16];
		return random.nextDouble() < data[chunkz][chunkx];
	}
	

}
