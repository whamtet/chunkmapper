package com.chunkmapper.column;

import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.LenteTreeWriter;

public class CroplandWithVegetation extends AbstractColumn {
	public static final Globcover TYPE = Globcover.CroplandWithVegetation;
	public static final int SHRUB = 0, SAVANNAH_TREE = 1;
	public final int treeType;
	public final byte cropType;
	public byte biome = Biome.Plains;
	public CroplandWithVegetation(int absx, int absz, byte cropType, HeightsReader heightsReader) {
		this(absx, absz, cropType, heightsReader, 10);
	}
	protected CroplandWithVegetation(int absx, int absz, byte cropType, HeightsReader heightsReader, int TREE_SPACING) {
		super(absx, absz, heightsReader);
		treeType = RANDOM.nextInt(2);
		this.cropType = cropType;
		if (StaticSobol.hasObject(absx, absz, TREE_SPACING)) {
			super.lenteTree = LenteTree.randomTree(LenteTree.CroplandWithVegetation);
//			treeHeight = treeType == SHRUB ? TreeWriter.getShrubHeight() : TreeWriter.getSavannaTreeHeight();						
//		} else {
//			treeHeight = 0;
		}
	}
//	public void addColumn(Chunk chunk) {
//		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
//		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);
//
//		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
//		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
//		chunk.Blocks[h-1][z][x] = Block.Grass.val;
////		chunk.Blocks[h-1][z][x] = Block.Farmland.val;
////		chunk.Data[h-1][z][x] = 7;
////		chunk.Blocks[h][z][x] = cropType;
////		chunk.Data[h][z][x] = 7; 
//	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {
		if (lenteTree != null)
		LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, lenteTree);
//		if (treeHeight != 0) {
//			boolean hasSnow = false;
//			if (treeType == SHRUB) {
//				TreeWriter.placeShrub(absx, absz, chunk, hasSnow, heightsReader, treeHeight);
//			} else {
//				TreeWriter.placeSavannaTree(absx, absz, chunk, heightsReader, treeHeight);
//			}
//		}
	}

}
