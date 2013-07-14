package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;

public class ClosedBroadleafDeciduous extends AbstractColumn {
	public static final Globcover TYPE = Globcover.ClosedBroadleafDeciduous;

	protected ClosedBroadleafDeciduous(int absx, int absz,
			HeightsReader heightsReader, int treeSpacing) {
		super(absx, absz, heightsReader);
		if (StaticSobol.hasObject(absx, absz, treeSpacing)) {
			super.treeHeight = TreeWriter.getForestTreeHeight();
			super.IS_FOREST = true;
		}
	}
	public ClosedBroadleafDeciduous(int absx, int absz, HeightsReader heightsReader) {
		this(absx, absz, heightsReader, 4);
	}
	
	public void addTree(Chunk chunk, HeightsReader heightsReader) {
//		if (treeHeight != 0) {
			TreeWriter.placeForestTree(absx, absz, chunk, heightsReader, treeHeight);
//		}
	}

}
