package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class Urban extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Urban;

	public Urban(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		super.IS_URBAN = true;
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
		chunk.Blocks[h-1][z][x] = Block.Stone_Brick.val;
	}

}
