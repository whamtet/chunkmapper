package com.chunkmapper.process;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import org.apache.commons.io.FileUtils;

import com.chunkmapper.chunk.ReadChunk;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.writer.LevelDat;

public class CropLand {
	private static BlockingQueue<File> files = new LinkedBlockingQueue<File>();
	//Crops land for mapping
	public static void main(String[] args) throws Exception {
		File src = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/everest");
		File dest = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/everest2");

		FileUtils.deleteDirectory(dest);
		FileUtils.copyDirectory(src, dest);
		//need to update name
		File loadedLevelDatFile = new File(dest, "level.dat");
		LevelDat loadedLevelDat = new LevelDat(loadedLevelDatFile, null);
		loadedLevelDat.setName("everest2");
		loadedLevelDat.save();

		File regionFolder = new File(dest, "region");
		for (File f : regionFolder.listFiles()) {
			if (f.getName().endsWith(".mca")) {
				files.add(f);
			}
		}
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							File f = files.poll();
							if (f == null)
								return;
							System.out.println(f);
							RegionFile regionFile = new RegionFile(f);
							for (int chunkx = 0; chunkx < 32; chunkx++) {
								for (int chunkz = 0; chunkz < 32; chunkz++) {
									if (regionFile.hasChunk(chunkx, chunkz)) {
										//need to crop
										DataInputStream in = regionFile.getChunkDataInputStream(chunkx, chunkz);
										ReadChunk chunk = new ReadChunk(NbtIo.read(in));
										in.close();

//										//now actually crop
										for (int y = 0; y < 128; y++) {
											for (int z = 0; z < 16; z++) {
												for (int x = 0; x < 16; x++) {
													chunk.blocks[y][z][x] = chunk.blocks[y+128][z][x];
													chunk.blocks[y+128][z][x] = 0;

													chunk.data[y][z][x] = chunk.data[y+128][z][x];
													chunk.data[y+128][z][x] = 0;
												}
											}
										}
										//write out again
										DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
										NbtIo.write(chunk.getRoot(), out);
										out.close();
									}
								}
							}
							regionFile.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			});
			t.start();
		}
	}
}
