package com.chunkmapper.column2;

import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.TreeWriter;
import com.chunkmapper.writer.LenteTreeWriter;

public class BroadleafEvergreen extends AbstractColumn {
	public static final Globcover TYPE = Globcover.BroadleafEvergreen;
	
	public BroadleafEvergreen(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		super.IS_FOREST = true;
		final int SPACING = 4;
		if (StaticSobol.hasObject(absx, absz, SPACING)) {
			lenteTree = LenteTree.randomTree(LenteTree.BoadleafEvergreen);
		} else {
			lenteTree = null;
		}
	}

	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {
		if (lenteTree != null)
			LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, lenteTree);
	}

}
