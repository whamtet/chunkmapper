package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;

public class BroadleafEvergreen extends AbstractColumn {
	public static final Globcover TYPE = Globcover.BroadleafEvergreen;
	
	public BroadleafEvergreen(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		final int SPACING = 4;
		if (StaticSobol.hasObject(absx, absz, SPACING)) {
			super.treeHeight = TreeWriter.getJungleTreeHeight();
			super.IS_FOREST = true;
		}
	}

	public void addTree(Chunk chunk, HeightsReader heightsReader) {
//		if (treeHeight != 0) {
			TreeWriter.placeJungleTree(absx, absz, chunk, heightsReader, treeHeight);
//		}
	}

}
