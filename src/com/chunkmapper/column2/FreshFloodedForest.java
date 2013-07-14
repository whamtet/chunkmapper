package com.chunkmapper.column2;

import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.LenteTreeWriter;

public class FreshFloodedForest extends AbstractColumn {
	public static final Globcover TYPE = Globcover.FreshFloodedForest;
	public final boolean hasWater;

	public FreshFloodedForest(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		super.IS_FOREST = true;
		hasWater = RANDOM.nextInt(2) == 0 && 
				h <= heightsReader.getHeightxz(absx-1, absz) && h <= heightsReader.getHeightxz(absx+1, absz) &&
				h <= heightsReader.getHeightxz(absx, absz-1) && h <= heightsReader.getHeightxz(absx, absz+1);
		if (StaticSobol.hasObject(absx, absz, 5)) {
			lenteTree = LenteTree.randomTree(LenteTree.FreshFloodedForest);
		}
		super.HAS_WATER = hasWater;
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = Block.Dirt.val;

		if (hasWater) {
			chunk.Blocks[h-1][z][x] = Block.Water.val;
		} else {
			chunk.Blocks[h-1][z][x] = Block.Leaves.val;
		}
	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {
		if (lenteTree != null) {
			chunk.setBlock(h, absz, absx, Block.Dirt.val);
			LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, lenteTree);
		}
	}

}
