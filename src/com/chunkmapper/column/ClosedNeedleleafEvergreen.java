package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;

public class ClosedNeedleleafEvergreen extends AbstractColumn {
	public static final Globcover TYPE = Globcover.ClosedNeedleleafEvergreen;
	public static final int BROADLEAF_TREE = 0, NEEDLELEAF_TREE = 1;

	protected ClosedNeedleleafEvergreen(int absx, int absz,
			HeightsReader heightsReader, int treeSpacing) {
		super(absx, absz, heightsReader);
		super.IS_FOREST = true;
		if (StaticSobol.hasObject(absx, absz, treeSpacing)) {
				treeHeight = TreeWriter.getPineTreeHeight();
		}
	}
	public ClosedNeedleleafEvergreen(int absx, int absz, HeightsReader heightsReader) {
		this(absx, absz, heightsReader, 4);
	}

	public void addTree(Chunk chunk, HeightsReader heightsReader) {
		//		if (treeHeight != 0) {
			TreeWriter.placePineTree(absx, absz, chunk, false, treeHeight, heightsReader);
	}

}
