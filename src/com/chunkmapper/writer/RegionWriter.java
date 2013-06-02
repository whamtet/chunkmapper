package com.chunkmapper.writer;

import java.io.DataOutputStream;
import java.io.File;

import net.minecraft.world.level.chunk.storage.RegionFile;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.Point;
import com.chunkmapper.PointManager;
import com.chunkmapper.ProgressManager;
import com.chunkmapper.Tasker;
import com.chunkmapper.chunk.Chunk;
import com.mojang.nbt.NbtIo;

public class RegionWriter extends Tasker {
	public static final int NUM_WRITING_THREADS = Runtime.getRuntime().availableProcessors() + 1;
	public final Point rootPoint;
	public final File regionFolder;

	public RegionWriter(PointManager pointManager, Point rootPoint, File regionFolder, GameMetaInfo metaInfo, ProgressManager progressManager) {
		super(NUM_WRITING_THREADS, pointManager, metaInfo, progressManager);
		this.rootPoint = rootPoint;
		this.regionFolder = regionFolder;
	}
	public void addRegion(int regionx, int regionz) {
		super.addTask(regionx, regionz);
		//always try, and try again buddy
	}
	@Override
	protected void doTask(Point task) throws Exception {
		int a = task.x, b = task.z;
		int regionx = task.x + rootPoint.x, regionz = task.z + rootPoint.z;
		
		File f = new File(regionFolder, "r." + a + "." + b + ".mca");
		System.out.println("trying to write chunk " + a + ", " + b);
		GlobcoverManager coverManager = new GlobcoverManager(regionx, regionz);
		
		if (coverManager.allWater) {
			System.out.println("all water: skipping");
			return;
		}

		RegionFile regionFile = new RegionFile(f);

		System.out.println("writing chunk " + a + ", " + b);
		//
		ArtifactWriter artifactWriter = new ArtifactWriter();
		for (int x = 0; x < 32; x++) {
			for (int z = 0; z < 32; z++) {
				if (Thread.interrupted()) {
					regionFile.close();
					f.delete();
					throw new InterruptedException();
				}
				Chunk chunk = coverManager.getChunk(x + regionx*32, z + regionz*32, x + a*32, z + b*32, artifactWriter);
				DataOutputStream stream = regionFile.getChunkDataOutputStream(x, z);
				NbtIo.write(chunk.getTag(), stream);
				stream.close();
			}
		}
		regionFile.close();
		
		System.out.println("finished chunk " + a + ", " + b);
	}

}
