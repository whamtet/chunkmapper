package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.DataFormatException;

import com.chunkmapper.admin.Utila;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.sections.POI;

public class DensityReader {
	private static class PopulationCenter {
		private final int absx, absz;
		private final double a, R;
		
		public PopulationCenter(int absx, int absz, int population) {
			this.absx = absx;
			this.absz = absz;
			R = 5000 / Utila.Y_SCALE * Math.cbrt(population / 50000.);
			a = population / R / R;
		}
		public double getPopulationDensity(int absx, int absz) {
			int i = absx - this.absx;
			int j = absz - this.absz;
			double r = Math.sqrt(i*i + j*j);
			return a * (1 - r / R) / Utila.Y_SCALE / Utila.Y_SCALE;
		}
	}
	private final ArrayList<PopulationCenter> populationCenters = new ArrayList<PopulationCenter>();
	public final int regionx, regionz;
	public DensityReader(Collection<POI> rootPOIs, int regionx, int regionz)  throws IOException, URISyntaxException, InterruptedException, DataFormatException {
		this.regionx = regionx;
		this.regionz = regionz;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				Collection<POI> pois = i == 0 && j == 0 && rootPOIs != null ? rootPOIs : POIParser.getPois(regionx, regionz);
				for (POI poi : pois) {
					if (poi.population != null) {
						populationCenters.add(new PopulationCenter(poi.point.x, poi.point.z, poi.population));
					}
				}
			}
		}
	}
	public DensityReader(int regionx2, int regionz2) throws IOException, URISyntaxException, InterruptedException, DataFormatException {
		this(null, regionx2, regionz2);
	}
	public double getDensityij(int i, int j) {
		return getDensityxz(j + regionx * 512, i + regionz * 512);
	}
	public boolean isUrbanij(int i, int j) {
		return isUrbanxz(j + regionx * 512, i + regionz * 512);
	}
	public double getDensityxz(int absx, int absz) {
		double density = 0;
		for (PopulationCenter c : populationCenters) {
			double d = c.getPopulationDensity(absx, absz);
			if (d > density)
				density = d;
		}
		return density;
	}
	public boolean isUrbanxz(int absx, int absz) {
		return getDensityxz(absx, absz) > 1e-3;
	}

}
