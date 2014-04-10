package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class OpenNeedleleaf extends ClosedNeedleleafEvergreen {
	public static final Globcover TYPE = Globcover.OpenNeedleleaf;
	public byte biome = Biome.Taiga;
	public OpenNeedleleaf(int absx, int absz,
			HeightsReader heightsReader) {
		super(absx, absz, heightsReader, 8);
		// TODO Auto-generated constructor stub
	}

}
