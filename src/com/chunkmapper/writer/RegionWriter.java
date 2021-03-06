package com.chunkmapper.writer;

import java.io.DataOutputStream;
import java.io.File;
import com.chunkmapper.FileValidator;
import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.Point;
import com.chunkmapper.Tasker;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PointManager;
import com.chunkmapper.manager.GlobcoverManager;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;

public class RegionWriter extends Tasker {
	public static final int NUM_WRITING_THREADS = numThreads();
	public final Point rootPoint;
	public final File regionFolder;
	private final GameMetaInfo gameMetaInfo;
	private final MappedSquareManager mappedSquareManager;
	private final PointManager pointManager;
	private final boolean gaiaMode;
	private final int verticalExaggeration;
	private final LevelDat levelDat;
	
	private static int numThreads() {
		int numThreads = Runtime.getRuntime().availableProcessors() / 2;
		if (numThreads < 1)
			return 1;
		return numThreads;
	}

	public RegionWriter(PointManager pointManager, Point rootPoint, File regionFolder, 
			GameMetaInfo metaInfo, MappedSquareManager mappedSquareManager, boolean gaiaMode, 
			int verticalExaggeration, LevelDat loadedLevelDat) {
		super(NUM_WRITING_THREADS, (a, b) -> {
			// TODO Auto-generated method stub
			Point playerPosition = null;
			if (pointManager != null)
				playerPosition = pointManager.getCurrentPlayerPosition();
			if (playerPosition == null) {
				return 0;
			}
			a = new Point(a.x * 512 + 256, a.z * 512 + 256);
			b = new Point(b.x * 512 + 256, b.z * 512 + 256);

			return a.distance(playerPosition) < b.distance(playerPosition) ? -1 : 1;
		});
		this.gaiaMode = gaiaMode;
		this.verticalExaggeration = verticalExaggeration;
		this.rootPoint = rootPoint;
		this.regionFolder = regionFolder;
		this.gameMetaInfo = metaInfo;
		this.mappedSquareManager = mappedSquareManager;
		this.pointManager = pointManager;
		this.levelDat = loadedLevelDat;
	}
	@Override
	protected void doTask(Point task) throws Exception {
		
		int a = task.x, b = task.z;
		int regionx = task.x + rootPoint.x, regionz = task.z + rootPoint.z;
		Point p = new Point(regionx, regionz);
		MyLogger.LOGGER.info("Writing point at " + p.toString());
		
		File f = new File(regionFolder, "r." + a + "." + b + ".mca");
		boolean writeRails = true;
		GlobcoverManager coverManager = new GlobcoverManager(regionx, regionz, verticalExaggeration, gaiaMode, writeRails);

		if (coverManager.allWater) {
			pointManager.flagPointCompletion(task);
			mappedSquareManager.addFinishedPoint(p);
			MyLogger.LOGGER.info("Wrote ocean point at " + p.toString()); 
			return;
		}

		RegionFile regionFile = new RegionFile(f);
		
		int playerChunkx = 0, playerChunkz = 0;
		Point playerPosition = null;
		boolean isRootPoint = a == 0 && b == 0;
		if (isRootPoint) {
			playerPosition = levelDat.getPlayerPosition();
			playerChunkx = Matthewmatics.mod(Matthewmatics.div(playerPosition.x, 16), 32);
			playerChunkz = Matthewmatics.mod(Matthewmatics.div(playerPosition.z, 16), 32);
		}

		for (int chunkx = 0; chunkx < 32; chunkx++) {
			for (int chunkz = 0; chunkz < 32; chunkz++) {
				if (Thread.interrupted()) {
					regionFile.close();
					f.delete();
					throw new InterruptedException();
				}
				int abschunkx = chunkx + regionx * 32, abschunkz = chunkz + regionz * 32;
				Chunk chunk = coverManager.getChunk(abschunkx, abschunkz, chunkx + a*32, chunkz + b*32);
				DataOutputStream stream = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), stream);
				stream.close();
				
				if (isRootPoint && chunkx == playerChunkx && chunkz == playerChunkz) {
					int x = playerPosition.x - playerChunkx * 16, z = playerPosition.z - playerChunkz * 16;
					int h = chunk.getMaxBlock(x, z) + 2;
					levelDat.setPlayerPosition(playerPosition.x, h, playerPosition.z);
					levelDat.save();
				}
			}
		}
		regionFile.close();
		FileValidator.setSupervalid(f);
		pointManager.flagPointCompletion(task);
		gameMetaInfo.incrementChunksMade();
		MyLogger.LOGGER.info("Wrote point at " + p.toString());
		if (mappedSquareManager != null)
			mappedSquareManager.addFinishedPoint(p);
                
	}

}
