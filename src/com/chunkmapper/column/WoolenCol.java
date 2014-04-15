package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.reader.HeightsReader;

public class WoolenCol extends AbstractColumn {

	public WoolenCol(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
	}
	@Override
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);
		
		for (int y = 0; y < h; y++) {
			chunk.Blocks[y][z][x] = Blocka.Wool;
		}
	}

}
