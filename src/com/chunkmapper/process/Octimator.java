package com.chunkmapper.process;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


import org.apache.commons.io.FileUtils;

import com.chunkmapper.Utila;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.LoadedLevelDat;

public class Octimator {
	private static final int FACTOR = 8;
	
	
	public static void main(String[] args) throws Exception {
		File regionSource = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/WoW trees grid world/region");
		File regionDestination = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/test2/region");
		prepareWorld("test2");
		process(regionSource, regionDestination);
	}
	private static void prepareWorld(String name) throws IOException {
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
		levelDat.setName("name");
		levelDat.save();
		
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		for (int chunkx = 0; chunkx < 16; chunkx++) {
			for (int chunkz = 0; chunkz < 16; chunkz++) {
				int[][] heights = new int[Utila.CHUNK_START + Utila.CHUNK_END][Utila.CHUNK_START + Utila.CHUNK_END];
				for (int i = 0; i < Utila.CHUNK_START + Utila.CHUNK_END; i++) {
					for (int j = 0; j < Utila.CHUNK_START + Utila.CHUNK_END; j++) {
						heights[i][j] = 0;
					}
				}
				
				for (int x = 0; x < 16; x++) {
					int h = chunkx * 16 + x;
					for (int z = 0; z < 16; z++) {
						heights[x + Utila.CHUNK_START][z + Utila.CHUNK_START] = h;
					}
				}
				Chunk chunk = new Chunk(chunkx, chunkz, heights, chunkx, chunkz);
				GenericWriter.addBedrock(chunk);
				
				DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), out);
				out.close();
			}
		}
		regionFile.close();
		System.out.println("done");
	}
		
	private static void process(File regionSource, File regionDestination) throws IOException {
		System.out.println("processing");
		
		ChunkCache chunkCache = new ChunkCache();
		for (File f : regionSource.listFiles()) {
			if (f.getName().endsWith(".mca")) {
				System.out.println(f.getName());
				String[] split = f.getName().split("\\.");
				int srcregionx = Integer.parseInt(split[1]);
				int srcregionz = Integer.parseInt(split[2]);
				
				RegionFile regionSrc = new RegionFile(f);
				for (int srcchunkx = 0; srcchunkx < 32; srcchunkx++) {
					for (int srcchunkz = 0; srcchunkz < 32; srcchunkz++) {
						if (regionSrc.hasChunk(srcchunkx, srcchunkz)) {
							//wowawuwa
							DataInputStream in = regionSrc.getChunkDataInputStream(srcchunkx, srcchunkz);
							CompoundTag root = NbtIo.read(in);
							in.close();
							NakedChunk naked = new NakedChunk(root);
							
							//get roots
							int abssrcchunkx = srcchunkx + srcregionx * 32;
							int abssrcchunkz = srcchunkz + srcregionz * 32;
							
							int abschunkx2 = Matthewmatics.div(abssrcchunkx, FACTOR);
							int abschunkz2 = Matthewmatics.div(abssrcchunkz, FACTOR);
							
							Chunk destination = chunkCache.getChunk(abschunkx2, abschunkz2);
							//now we have to go through each one.
							for (int y = 0; y < 256; y += FACTOR) {
								for (int z = 0; z < 16; z += FACTOR) {
									for (int x = 0; x < 16; x += FACTOR) {
//										ModeCalculator blockCal = new ModeCalculator();
//										ModeCalculator dataCal = new ModeCalculator();
//										for (int yd = 0; yd < FACTOR; yd++) {
//											for (int zd = 0; zd < FACTOR; zd++) {
//												for (int xd = 0; xd < FACTOR; xd++) {
//													blockCal.addValue(naked.Blocks[y+yd][z+zd][x+xd]);
//													dataCal.addValue(naked.Blocks[y+yd][z+zd][x+xd]);
//												}
//											}
//										}
										int absz = abssrcchunkz * 16 + z;
										int absx = abssrcchunkx * 16 + x;
										int z2 = Matthewmatics.div(absz, FACTOR);
										int x2 = Matthewmatics.div(absx, FACTOR);
										
										int z3 = z2 - destination.z0, x3 = x2 - destination.x0;
										int y3 = y / FACTOR;
//										destination.Blocks[y3][z3][x3] = blockCal.getMode();
//										destination.Data[y3][z3][x3] = dataCal.getMode();
										destination.Blocks[y3][z3][x3] = naked.Blocks[y][z][x];
										destination.Data[y3][z3][x3] = naked.Data[y][z][x];
										
									}
								}
							}
						}
					}
				}
			}
		}
		for (Chunk chunk : chunkCache.chunks.values()) {
			int regionx = Matthewmatics.div(chunk.abschunkx, 32);
			int regionz = Matthewmatics.div(chunk.abschunkz, 32);
			RegionFile regionFile = new RegionFile(new File(regionDestination, "r." + regionx + "." + regionz + ".mca"));
			int chunkx = Matthewmatics.mod(chunk.abschunkx, 32);
			int chunkz = Matthewmatics.mod(chunk.abschunkz, 32);
			
			DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
			NbtIo.write(chunk.getTag(), out);
			out.close();
			regionFile.close();
		}
		
	}

}
