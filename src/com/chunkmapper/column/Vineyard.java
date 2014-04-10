package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.reader.HeightsReader;

public class Vineyard extends AbstractColumn {
	public byte biome = Biome.Plains;
	public Vineyard(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
		chunk.Blocks[h-1][z][x] = Block.Grass.val;

		int i = RANDOM.nextInt(5);
		if (i == 0) {
			chunk.Blocks[h][z][x] = Block.Long_Grass.val;
			chunk.Data[h][z][x] = DataSource.Long_Grass.val;
		}

		if (x % 3 == 0) {
			//need to add a row, bitch!
			for (i = h; i < h + 2; i++) {
				chunk.Blocks[i][z][x] = RANDOM.nextInt(2) == 0 ? Blocka.Leaves : Blocka.Fence;
			}
			//and feathers
			if (RANDOM.nextInt(10) == 0) {
				chunk.Blocks[h+2][z][x] = Blocka.Leaves;
			}
		} else {
		}
	}


}
