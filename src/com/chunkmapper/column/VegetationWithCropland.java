package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class VegetationWithCropland extends CroplandWithVegetation {
	public byte biome = Biome.Forest;
	public static final Globcover TYPE = Globcover.CroplandWithVegetation;

	public VegetationWithCropland(int absx, int absz, byte cropType,
			HeightsReader heightsReader) {
		super(absx, absz, cropType, heightsReader, 5);
		// TODO Auto-generated constructor stub
	}
	

}
