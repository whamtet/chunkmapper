package com.chunkmapper.column2;

import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.math.StaticSobol;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.LenteTreeWriter;

public class Coast extends AbstractColumn {

	public Coast(int absx, int absz) {
		super(absx, absz);
		if (StaticSobol.hasObject(absx, absz, 40)) {
			lenteTree = LenteTree.randomTree(LenteTree.Coast);
		}
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[0][z][x] = Block.Bedrock.val;
		chunk.Blocks[1][z][x] = Block.Sand.val;
		chunk.Blocks[2][z][x] = Block.Sand.val;
		chunk.Blocks[3][z][x] = Block.Sand.val;

		for (int i = 4; i < 256; i++) {
			chunk.Blocks[i][z][x] = 0;
		}
	}
	public void addTree(Chunk chunk, HeightsReader heightsReader) throws IOException {
		if (lenteTree != null)
		LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, lenteTree);
	}

}
