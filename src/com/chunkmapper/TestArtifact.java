package com.chunkmapper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.enumeration.Villager;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.UniformHeightsReader;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.LenteTreeWriter;
import com.chunkmapper.writer.LevelDat;
import com.chunkmapper.writer.MobWriter;
import com.chunkmapper.writer.SchematicArtifactWriter;

public class TestArtifact {

	public TestArtifact() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Random random = new Random();
		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/house");
		FileUtils.deleteDirectory(gameFolder);
		File regionFolder = new File(gameFolder, "region");
		regionFolder.mkdirs();
		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		LevelDat loadedLevelDat = new LevelDat(loadedLevelDatFile);
		//		ParallelWriter writer = new ParallelWriter(0, 0, 0, 0, "house", true);
		loadedLevelDat.setPlayerPosition(0, 105, 0);
		loadedLevelDat.setName("house");
		loadedLevelDat.save();	
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		int[][] heights = new int[24][24];
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				heights[i][j] = 100;
			}
		}
		for (int chunkx = 0; chunkx < 32; chunkx++) {
			for (int chunkz = 0; chunkz < 32; chunkz++) {
				Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
				GenericWriter.addGrass(chunk);

				DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), out);
				out.close();
			}
		}
		regionFile.close();

		//		PrintStream out2 = new PrintStream("cow.txt");
		//		chunk.Entities.print(out2);
		//		out2.close();
		//		Runtime.getRuntime().exec("open cow.txt");

		System.out.println("done");
	}

}
