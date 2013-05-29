package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class River extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Water;

	public River(int absx, int absz, HeightsReader heightsReader) {
		
		super(absx, absz, heightsReader);
		super.HAS_WATER = true;
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);
		
		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = RANDOM.nextInt(2) == 0 ?
				Block.Gravel.val : Block.Clay.val;
		chunk.Blocks[h-1][z][x] = Block.Water.val;
	}

}
