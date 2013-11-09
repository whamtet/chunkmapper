package com.chunkmapper.column;

import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.LenteTreeWriter;

public class Shrubland extends AbstractColumn {
	public static final Globcover TYPE = Globcover.Shrubland;

	protected Shrubland(int absx, int absz,
			HeightsReader heightsReader, int treeSpacing) {
		super(absx, absz, heightsReader);
		if (StaticSobol.hasObject(absx, absz, treeSpacing)) {
				lenteTree = LenteTree.randomTree(LenteTree.Shrubland);
		}
		super.biome = Biome.Savanna;
	}
	public Shrubland(int absx, int absz, HeightsReader heightsReader) {
		this(absx, absz, heightsReader, 4);
	}

	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {
		if (lenteTree != null)
			LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, lenteTree);
	}

}
