package com.chunkmapper.column2;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public abstract class AbstractColumn {
	public static final Globcover TYPE = Globcover.NoData;
	public static final Random RANDOM = new Random();
	public final int absx, absz, h;
	public int treeHeight = 0;
	public boolean IS_URBAN = false, IS_FOREST = false, HAS_WATER = false;
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
	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) {} //does nothing at this stage

}
