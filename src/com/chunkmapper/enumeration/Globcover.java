package com.chunkmapper.enumeration;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum Globcover {
	IrrigatedCrops, RainfedCrops, CroplandWithVegetation, VegetationWithCropland, BroadleafEvergreen,
	ClosedBroadleafDeciduous, OpenBroadleafDeciduous, ClosedNeedleleafEvergreen, OpenNeedleleaf,
	MixedBroadNeedleleaf, ForestShrublandWithGrass, GrassWithForestShrubland, Shrubland, Grassland,
	SparseVegetation, FreshFloodedForest, SalineFloodedForest, FloodedGrassland, Urban, Bare, Water, Snow, NoData;

	public static Globcover[] makeArray(File f) throws IOException {
		DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
		//read 8 byte header
		dataStream.skip(8);
		while (true) {
			int len = dataStream.readInt();
			char[] nameArr = new char[4];
			nameArr[0] = (char) dataStream.readByte();
			nameArr[1] = (char) dataStream.readByte();
			nameArr[2] = (char) dataStream.readByte();
			nameArr[3] = (char) dataStream.readByte();
			String name = new String(nameArr);
			
			if (name.equals("PLTE")) {
				Globcover[] pallette = new Globcover[256];
				for (int i = 0; i < 256; i++) {
					int r = dataStream.readByte();
					int g = dataStream.readByte();
					int b = dataStream.readByte();
					if (r < 0) r += 256;
					if (g < 0) g += 256;
					if (b < 0) b += 256;
					
					if (r == 170 && g == 240 && b == 240) {
						pallette[i] = Globcover.IrrigatedCrops;
					} else if (r == 255 && g == 255 && b == 100) {
						pallette[i] = Globcover.RainfedCrops;
					} else if (r == 220 && g == 240 && b == 100) {
						pallette[i] = Globcover.CroplandWithVegetation;
					} else if (r == 205 && g == 205 && b == 102) {
						pallette[i] = Globcover.VegetationWithCropland;
					} else if (r == 0 && g == 100 && b == 0) {
						pallette[i] = Globcover.BroadleafEvergreen;
					} else if (r == 0 && g == 160 && b == 0) {
						pallette[i] = Globcover.ClosedBroadleafDeciduous;
					} else if (r == 170 && g == 200 && b == 0) {
						pallette[i] = Globcover.OpenBroadleafDeciduous;
					} else if (r == 0 && g == 60 && b == 0) {
						pallette[i] = Globcover.ClosedNeedleleafEvergreen;
					} else if (r == 40 && g == 100 && b == 0) {
						pallette[i] = Globcover.OpenNeedleleaf;
					} else if (r == 120 && g == 130 && b == 0) {
						pallette[i] = Globcover.MixedBroadNeedleleaf;
					} else if (r == 140 && g == 160 && b == 0) {
						pallette[i] = Globcover.ForestShrublandWithGrass;
					} else if (r == 190 && g == 150 && b == 0) {
						pallette[i] = Globcover.GrassWithForestShrubland;
					} else if (r == 150 && g == 100 && b == 0) {
						pallette[i] = Globcover.Shrubland;
					} else if (r == 255 && g == 180 && b == 50) {
						pallette[i] = Globcover.Grassland;
					} else if (r == 255 && g == 235 && b == 175) {
						pallette[i] = Globcover.SparseVegetation;
					} else if (r == 0 && g == 120 && b == 90) {
						pallette[i] = Globcover.FreshFloodedForest;
					} else if (r == 0 && g == 150 && b == 120) {
						pallette[i] = Globcover.SalineFloodedForest;
					} else if (r == 0 && g == 220 && b == 130) {
						pallette[i] = Globcover.FloodedGrassland;
					} else if (r == 195 && g == 20 && b == 0) {
						pallette[i] = Globcover.Urban;
					} else if (r == 255 && g == 245 && b == 215) {
						pallette[i] = Globcover.Bare;
					} else if (r == 0 && g == 70 && b == 200) {
						pallette[i] = Globcover.Water;
					} else if (r == 255 && g == 255 && b == 255) {
						pallette[i] = Globcover.Snow;
					} else {
						pallette[i] = Globcover.NoData;
					}
				}
				dataStream.close();
				return pallette;
			} else {
				dataStream.skip(len + 4);
			}
		}
	}

}
