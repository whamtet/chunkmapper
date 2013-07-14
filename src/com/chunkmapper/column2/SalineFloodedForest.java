package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;

public class SalineFloodedForest extends AbstractColumn {
	public static final Globcover TYPE = Globcover.SalineFloodedForest;
	public final boolean hasWater;
	private final int treeHeight;

	public SalineFloodedForest(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		super.IS_FOREST = true;
		hasWater = RANDOM.nextInt(3) > 0 && 
				h <= heightsReader.getHeightxz(absx-1, absz) && h <= heightsReader.getHeightxz(absx+1, absz) &&
				h <= heightsReader.getHeightxz(absx, absz-1) && h <= heightsReader.getHeightxz(absx, absz+1);
		if (StaticSobol.hasObject(absx, absz, 5)) {
			treeHeight = TreeWriter.getSavannaTreeHeight();
		} else {
			treeHeight = 0;
		}
		super.HAS_WATER = hasWater;
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Clay.val;
		chunk.Blocks[h-2][z][x] = Block.Sand.val;
		
		if (hasWater) {
			chunk.Blocks[h-1][z][x] = Block.Water.val;
		} else {
			chunk.Blocks[h-1][z][x] = Block.Leaves.val;
		}
	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) {
		if (treeHeight != 0) {
		chunk.setBoth(h, absz, absx, Block.Wood.val, DataSource.Jungle.val);
		TreeWriter.placeSavannaTree(absx, absz, chunk, heightsReader, treeHeight);
		}
	}

}
