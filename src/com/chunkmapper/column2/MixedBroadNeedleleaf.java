package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;

public class MixedBroadNeedleleaf extends AbstractColumn {
	public static final Globcover TYPE = Globcover.MixedBroadNeedleleaf;
	public static final int BROADLEAF_TREE = 0, NEEDLELEAF_TREE = 1;
	public final int treeType;

	protected MixedBroadNeedleleaf(int absx, int absz,
			HeightsReader heightsReader, int treeSpacing) {
		super(absx, absz, heightsReader);
		super.IS_FOREST = true;
		treeType = RANDOM.nextInt(2);
		if (StaticSobol.hasObject(absx, absz, treeSpacing)) {
			if (treeType == BROADLEAF_TREE) {
				treeHeight = TreeWriter.getForestTreeHeight();
			} else {
				treeHeight = TreeWriter.getPineTreeHeight();
			}
		}
	}
	public MixedBroadNeedleleaf(int absx, int absz, HeightsReader heightsReader) {
		this(absx, absz, heightsReader, 4);
	}

	public void addTree(Chunk chunk, HeightsReader heightsReader) {
		//		if (treeHeight != 0) {
		if (treeType == BROADLEAF_TREE) {
			TreeWriter.placeForestTree(absx, absz, chunk, heightsReader, treeHeight);
		} else {
			TreeWriter.placePineTree(absx, absz, chunk, false, treeHeight, heightsReader);
		}
	}

}
