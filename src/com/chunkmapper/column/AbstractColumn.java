package com.chunkmapper.column;

import java.io.IOException;
import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.reader.HeightsReader;

public abstract class AbstractColumn {
	/*
	 * A column is a 1x1x256 high column of Minecraft blocks belonging to a certain geographic biome.
	 * The main biome data source is Globcover, which has 300m resolution (10x10 blocks in map).
	 */
	
	public static final Random RANDOM = new Random();
	public final int absx, absz, h;
	public LenteTree lenteTree;
	public boolean IS_URBAN = false, IS_FOREST = false, HAS_WATER = false;
	public boolean HAS_ANIMALS = false;
	public byte biome = Biome.Ocean;
	protected AbstractColumn(int absx, int absz) {
		this.absx = absx;
		this.absz = absz;
		h = 0; // we don't care about h in this case
	}
	protected AbstractColumn(int absx, int absz, HeightsReader heightsReader) {
		this.absx = absx;
		this.absz = absz;
		h = heightsReader.getHeightxz(absx, absz);
	}
	//default methods
	public void addColumn(Chunk chunk) {
		addColumn(chunk, false, false);
	}
	protected void addColumn(Chunk chunk, boolean dandelion, boolean rose) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
		chunk.Blocks[h-1][z][x] = Block.Grass.val;
		int i = RANDOM.nextInt(3);
		if (i > 0) {
			chunk.Blocks[h][z][x] = Block.Long_Grass.val;
			chunk.Data[h][z][x] = i == 1 ? DataSource.Fern.val : DataSource.Long_Grass.val; 
		}
		if (dandelion && RANDOM.nextInt(100) == 0) {
			chunk.Blocks[h][z][x] = Block.Dandelion.val;
		}
		if (rose && RANDOM.nextInt(100) == 0) {
			chunk.Blocks[h][z][x] = Blocka.Rose;
		}
	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {} //does nothing at this stage

}
