package com.chunkmapper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.column.Orchard;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.UniformHeightsReader;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.LenteTreeWriter;
import com.chunkmapper.writer.LevelDat;

public class TestArtifact {

	public TestArtifact() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/house");
		File src = new File("resources/level.dat");
		File regionFolder = new File(gameFolder, "region");
		File loadedLevelDatFile = new File("/Users/matthewmolloy/Library/Application Suppport/minecraft/saves/house/level.dat");
		LevelDat loadedLevelDat = new LevelDat(loadedLevelDatFile);
//		ParallelWriter writer = new ParallelWriter(0, 0, 0, 0, "house", true);
		loadedLevelDat.setPlayerPosition(512, 250, 512);
		loadedLevelDat.save();
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		int[][] heights = new int[24][24];
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				heights[i][j] = 4;
			}
		}
		int chunkx = 0, chunkz = 0;
		Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
		GenericWriter.addBedrock(chunk);
		
		HeightsReader heightsReader = new UniformHeightsReader();
//		for (int i = 0; i < 16; i++) {
//			for (int j = 0; j < 16; j++) {
//				Vineyard col = new Vineyard(j, i, heightsReader);
//				col.addColumn(chunk);
//			}
//		}
		for (int i = -5; i < 21; i++) {
			for (int j = -5; j < 21; j++) {
				Orchard col = new Orchard(j, i, heightsReader);
				if (0 <= i && i < 16 && 0 <= j && j < 16) {
					col.addColumn(chunk);
				}
				col.addTree(chunk, heightsReader);
			}
		}
		
		DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
		NbtIo.write(chunk.getTag(), out);
		out.close();
		regionFile.close();
		
//		PrintStream out2 = new PrintStream("cow.txt");
//		chunk.Entities.print(out2);
//		out2.close();
//		Runtime.getRuntime().exec("open cow.txt");

		System.out.println("done");
	}
	//write all the Lente trees
	private static void writeTree(int chunkx, int chunkz, LenteTree tree, int[][] heights, RegionFile regionFile) throws IOException {
		Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
		GenericWriter.addBedrock(chunk, 0);
		HeightsReader heightsReader  = new UniformHeightsReader();
		int absx = chunkx*16 + 8, absz = chunkz * 16 + 8;
		LenteTreeWriter.placeLenteTree(absx, absz, chunk, heightsReader, tree);
		ArtifactWriter.addSign(chunk, 4, 0, 0, tree.toString().split("_"));
		
		DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
		NbtIo.write(chunk.getTag(), out);
		out.close();
		
		
	}


}
