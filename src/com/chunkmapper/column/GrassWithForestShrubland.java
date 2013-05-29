package com.chunkmapper.column;

import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class GrassWithForestShrubland extends MixedBroadNeedleleaf {
	public static final Globcover TYPE = Globcover.GrassWithForestShrubland;

	public GrassWithForestShrubland(int absx, int absz,
			HeightsReader heightsReader) {
		super(absx, absz, heightsReader, 12);
		// TODO Auto-generated constructor stub
	}

}
