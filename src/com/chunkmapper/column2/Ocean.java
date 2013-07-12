package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class Ocean extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Water;

	public Ocean(int absx, int absz) {
		super(absx, absz);
		super.HAS_WATER = true;
		// TODO Auto-generated constructor stub
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);
		
		chunk.Blocks[0][z][x] = Block.Bedrock.val;
		chunk.Blocks[1][z][x] = Block.Sand.val;
		chunk.Blocks[2][z][x] = Block.Water.val;
		chunk.Blocks[3][z][x] = Block.Water.val;
		
		for (int i = 4; i < 256; i++) {
			chunk.Blocks[i][z][x] = 0;
		}
	}

}
