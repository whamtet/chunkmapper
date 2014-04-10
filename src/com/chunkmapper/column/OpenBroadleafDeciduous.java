package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class OpenBroadleafDeciduous extends ClosedBroadleafDeciduous {
	public static final Globcover TYPE = Globcover.ClosedBroadleafDeciduous;
	public byte biome = Biome.Forest;
	public OpenBroadleafDeciduous(int absx, int absz,
			HeightsReader heightsReader) {
		super(absx, absz, heightsReader, 8);
		// TODO Auto-generated constructor stub
	}

}
