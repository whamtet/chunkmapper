package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.parser.LakeParser;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.sections.Lake;
import com.chunkmapper.sections.Section;


public class XapiLakeReader2 {
	private boolean[][] hasWater = new boolean[512][512];

	public boolean hasWaterij(int i, int j) {
		return hasWater[i][j];
	}


	public XapiLakeReader2(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException {
		
		ArrayList<Lake> lakes = LakeParser.getLakes(regionx, regionz); 
		ArrayList<Section> sections = new ArrayList<Section>();
		for (Lake lake : lakes) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point previousPoint = null;
			for (Point point : lake.points) {
				if (!point.equals(previousPoint)) {
					points.add(point);
					previousPoint = point;
				}
			}
			if (points.size() < 2) {
				System.out.println("continuing");
				continue;
			}
			
			int size = points.size();
			for (int i = 0; i < size - 1; i++) {
				Point previous = i == 0 ? points.get(size - 2) : points.get(i-1);
				Point a = points.get(i), b = points.get(i+1);
				Point next = i == size - 2 ? points.get(0) : points.get(i + 2);
				Section section = new Section(previous, a, b, next);
				if (!section.isHorizontal) {
					sections.add(section);
				}
			}
		}
		System.out.println("***");
		//now go through each row
		for (int z = 0; z < 512; z++) {
			int absz = z + regionz * 512;
//			TreeSet<Integer> intersections = new TreeSet<Integer>();
			ArrayList<Integer> intersections = new ArrayList<Integer>();
			for (Section section : sections) {
				Integer intersection = section.getIntersection(absz);
				if (intersection != null) {
					intersections.add(intersection);
				}
			}
			
			Collections.sort(intersections);
			ArrayList<Point> pairs = new ArrayList<Point>();
			for (int i = 1; i < intersections.size(); i += 2) {
				pairs.add(new Point(intersections.get(i-1), intersections.get(i)));
			}
			
			for (Point pair : pairs) {
				int a = pair.x - regionx * 512, b = pair.z - regionx * 512;
				if (a < 0)
					a = 0;
				if (b > 511)
					b = 511;
				for (int x = a; x <= b; x++) {
					hasWater[z][x] = true;
				}
			}

		}
	}

	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("queenstown, nz");
		double[] latlon = Nominatim.getPoint("te anau, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		XapiLakeReader2 reader = new XapiLakeReader2(regionx, regionz);
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				pw.println(reader.hasWaterij(z, x) ? 1 : 0);
			}
		}
		pw.close();
		System.out.println("done");
	}

}
