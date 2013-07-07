package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.parser.CoastlineParser;
import com.chunkmapper.sections.Coastline;
import com.chunkmapper.sections.Section;

public class XapiCoastlineReader {
	private boolean[][] hasWater = new boolean[512][512];
//	private int[][] data = new int[512][512];

	public boolean hasWaterij(int i, int j) {
		return hasWater[i][j];
	}
	public XapiCoastlineReader(int regionx, int regionz) throws IOException {
		
		HashSet<Coastline> coastlines = CoastlineParser.getCoastlines(regionx, regionz);
		if (coastlines.size() == 0) {
			return;
		}
		ArrayList<Coastline> connectedCoastlines = new ArrayList<Coastline>();
		boolean changesHaveBeenMade = true;
		while (changesHaveBeenMade) {
			changesHaveBeenMade = false;
			for 
		}
		ArrayList<Section> sections = new ArrayList<Section>();
		for (Coastline coastline : coastlines) {
			
		}
	}
}
