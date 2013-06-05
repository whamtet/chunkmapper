package com.chunkmapper;

import java.io.DataOutputStream;
import java.io.File;

import net.minecraft.world.level.chunk.storage.RegionFile;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.CircleRail;
import com.chunkmapper.enumeration.StraightRail;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
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
		loadedLevelDat.setPlayerPosition(0, 250, 0);
		loadedLevelDat.save();
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		int[][] heights = new int[24][24];
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				heights[i][j] = 4;
			}
		}
		Chunk chunk = new Chunk(0, 0, heights, 0, 0);
		GenericWriter.addBedrock(chunk, 0);
		testRails(chunk);
//		ArtifactWriter.placeMarket(chunk);
//		ArtifactWriter.placePrison(chunk);
//		ArtifactWriter.addTunnelIntoTheUnknown(chunk);
//		ArtifactWriter.addHouse(chunk);
//		ArtifactWriter.placeLookout(chunk);
//		for (int i = 0; i < 10; i++) {
//			MobWriter.addAnimal(chunk, "Cow");
//			MobWriter.addAnimal(chunk, "Sheep");
//			MobWriter.addAnimal(chunk, "Chicken");
//		}
//		ArtifactWriter.placeLibrary(chunk);

		DataOutputStream out = regionFile.getChunkDataOutputStream(0, 0);
		NbtIo.write(chunk.getTag(), out);
		out.close();
		regionFile.close();

		System.out.println("done");

	}
	private static void testRails(Chunk chunk) {
		ArtifactWriter artifactWriter = new ArtifactWriter();
		for (int i = 0; i < 16; i++) {
			artifactWriter.placeRail(i, i, chunk, 4, CircleRail.Three.val, false, false);
			if (i < 15) {
				artifactWriter.placeRail(i+1, i, chunk, 4, CircleRail.One.val, false, false);
			}
		}
	}

}
