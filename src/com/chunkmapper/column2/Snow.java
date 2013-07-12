package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class Snow extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Snow;

	public Snow(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		// TODO Auto-generated constructor stub
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Snow_Block.val;
		chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
		chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
	}

}
