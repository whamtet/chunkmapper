package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class Grassland extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Grassland;

	public Grassland(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		super.biome = Biome.Savanna;
		// TODO Auto-generated constructor stub
	}

}
