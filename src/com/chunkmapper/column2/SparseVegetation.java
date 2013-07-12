package com.chunkmapper.column2;

import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class SparseVegetation extends Shrubland {
	public static final Globcover TYPE = Globcover.SparseVegetation;

	public SparseVegetation(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader, 40);
	}

}
