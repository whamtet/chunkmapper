package com.chunkmapper.worldpainter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;

import javax.vecmath.Point3i;

import net.minecraft.world.level.chunk.storage.RegionFile;

import org.apache.commons.io.FileUtils;
import org.pepsoft.minecraft.Material;
import org.pepsoft.worldpainter.layers.bo2.Schematic;

import com.chunkmapper.Utila;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.LoadedLevelDat;
import com.mojang.nbt.NbtIo;

public class TestSchematic {
	private static HashSet<Integer> set = new HashSet<Integer>();

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		makeWorld("test");
	}
	private static void addTree(Chunk chunk, File f) throws IOException {
		String name = f.getName().split("\\.")[0];
		ArtifactWriter.addSign(chunk, 4, 15, 0, name.split(" "));
		Schematic s = Schematic.load(f);
		Point3i dimensions = s.getDimensions();
		int ymax = dimensions.z;
		int xmax = Math.min(16, dimensions.x);
		int zmax = Math.min(16, dimensions.y);
		
		for (int y = 0; y < ymax; y++) {
			for (int x = 0; x < xmax; x++) {
				for (int z = 0; z < zmax; z++) {
					if (s.getMask(x, z, y)) {
						Material m = s.getMaterial(x, z, y);
						chunk.Blocks[y+4][z][x] = (byte) m.getBlockType();
						chunk.Data[y+4][z][x] = (byte) m.getData();
						if (m.getBlockType() == Blocka.Leaves) {
							set.add(m.getData());
						}
					}
				}
			}
		}
	}

	private static void makeWorld(String name) throws IOException {

		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/" + name);
		FileUtils.deleteDirectory(gameFolder);
		File regionFolder = new File(gameFolder, "region");
		regionFolder.mkdirs();
		File src = new File("resources/level.dat");
		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		FileUtils.copyFile(src, loadedLevelDatFile);
		LoadedLevelDat levelDat = new LoadedLevelDat(loadedLevelDatFile);
		levelDat.setPlayerPosition(0, 10, 0);
		levelDat.setName(name);
		levelDat.save();
		
		FilenameFilter schematicFilter = new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				if (arg1.contains("Sandbox tree"))
					return false;
				return arg1.endsWith(".schematic");
			}
		};
		
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		
		for (int chunkx = 0; chunkx < 3; chunkx++) {
			File[] treeList;
			switch(chunkx) {
			case 0:
				treeList = (new File("resources/trees/tropical")).listFiles(schematicFilter);
				break;
			case 1:
				treeList = (new File("resources/trees/broadleaf")).listFiles(schematicFilter);
				break;
			default:
				treeList = (new File("resources/trees/needleleaf")).listFiles(schematicFilter);
			}
			for (int chunkz = 0; chunkz < 32; chunkz++) {
				int[][] heights = new int[Utila.CHUNK_START + Utila.CHUNK_END][Utila.CHUNK_START + Utila.CHUNK_END];
				for (int i = 0; i < Utila.CHUNK_START + Utila.CHUNK_END; i++) {
					for (int j = 0; j < Utila.CHUNK_START + Utila.CHUNK_END; j++) {
						heights[i][j] = 0;
					}
				}
				
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						heights[x + Utila.CHUNK_START][z + Utila.CHUNK_START] = 4;
					}
				}
				Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
				GenericWriter.addBedrock(chunk, 0);
				int i = chunkz;
				if (i < treeList.length) {
					addTree(chunk, treeList[i]);
				}
				
				DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), out);
				out.close();
			}
		}
		regionFile.close();
		System.out.println(set);
		System.out.println("done");
	}
}
