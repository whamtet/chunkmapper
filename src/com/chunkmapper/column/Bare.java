package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class Bare extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Bare;
	public final byte surfaceType;

	public Bare(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		if (h > 35) {
			surfaceType = Blocka.Gravel;
		} else {
			surfaceType = RANDOM.nextInt(2) == 0 ?
					Block.Sand.val : Block.Sandstone.val;
		}
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-2][z][x] = surfaceType;
		chunk.Blocks[h-1][z][x] = surfaceType;

	}

}
