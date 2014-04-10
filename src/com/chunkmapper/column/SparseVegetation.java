package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class SparseVegetation extends Shrubland {
	public static final Globcover TYPE = Globcover.SparseVegetation;
	public byte biome = Biome.Plains;

	public SparseVegetation(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader, 40);
	}

}
