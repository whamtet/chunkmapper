package com.chunkmapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import net.minecraft.world.level.chunk.storage.RegionFile;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.LoadedLevelDat;
import com.chunkmapper.writer.MobWriter;
import com.mojang.nbt.CompoundTag;
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
		loadedLevelDat.setPlayerPosition(0, 250, 0);
		loadedLevelDat.save();
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		printAndQuit(regionFile);
		int[][] heights = new int[24][24];
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				heights[i][j] = 4;
			}
		}
		Chunk chunk = new Chunk(0, 0, heights, 0, 0);
		GenericWriter.addBedrock(chunk, 0);
//		ArtifactWriter.placeMarket(chunk);
//		ArtifactWriter.placePrison(chunk);
//		ArtifactWriter.addTunnelIntoTheUnknown(chunk);
//		ArtifactWriter.addHouse(chunk);
//		ArtifactWriter.placeLookout(chunk);
		for (int i = 0; i < 10; i++) {
			MobWriter.addAnimal(chunk, "Horse");
//			MobWriter.addAnimal(chunk, "Cow");
//			MobWriter.addAnimal(chunk, "Sheep");
//			MobWriter.addAnimal(chunk, "Chicken");
		}
//		ArtifactWriter.placeLibrary(chunk);

		DataOutputStream out = regionFile.getChunkDataOutputStream(0, 0);
		NbtIo.write(chunk.getTag(), out);
		out.close();
		regionFile.close();

		System.out.println("done");

	}

	private static void printAndQuit(RegionFile regionFile) throws IOException {
		DataInputStream in = regionFile.getChunkDataInputStream(0, 0);
		CompoundTag tag = NbtIo.read(in);
		in.close();
		PrintStream out = new PrintStream("out.txt");
		tag.print(out);
		out.close();
		regionFile.close();
		Runtime.getRuntime().exec("open out.txt");
		System.exit(0);
		
	}


}
