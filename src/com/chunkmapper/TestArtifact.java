package com.chunkmapper;

import java.io.DataOutputStream;
import java.io.File;

import net.minecraft.world.level.chunk.storage.RegionFile;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.HorseWriter;
import com.chunkmapper.writer.LoadedLevelDat;
import com.mojang.nbt.NbtIo;

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
		FileUtils.copyFile(src, loadedLevelDatFile);
		LoadedLevelDat loadedLevelDat = new LoadedLevelDat(loadedLevelDatFile);
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
		int chunkx = 2, chunkz = 3;
		Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
		GenericWriter.addBedrock(chunk, 0);
		ArtifactWriter.addRugbyField(chunk);
//		ArtifactWriter.placeMarket(chunk);
//		ArtifactWriter.placePrison(chunk);
//		ArtifactWriter.addTunnelIntoTheUnknown(chunk);
//		ArtifactWriter.addHouse(chunk);
//		ArtifactWriter.placeLookout(chunk);
//		for (int i = 0; i < 10; i++) {
//			HorseWriter.addHorse(chunk);
//			MobWriter.addAnimal(chunk, "Cow");
//			MobWriter.addAnimal(chunk, "Sheep");
//			MobWriter.addAnimal(chunk, "Chicken");
//		}
//		ArtifactWriter.placeLibrary(chunk);

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


}
