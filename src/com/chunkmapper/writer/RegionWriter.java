package com.chunkmapper.writer;

import java.io.DataOutputStream;
import java.io.File;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;


import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.MappedSquareManager;
import com.chunkmapper.Point;
import com.chunkmapper.PointManager;
import com.chunkmapper.Tasker;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.manager.GlobcoverManager;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;

public class RegionWriter extends Tasker {
	public static final int NUM_WRITING_THREADS = Runtime.getRuntime().availableProcessors() + 1;
	public final Point rootPoint;
	public final File regionFolder;
	private final UberDownloader uberDownloader;
	private final GameMetaInfo gameMetaInfo;
	private final MappedSquareManager mappedSquareManager;
	private final PointManager pointManager;
	private final int verticalExaggeration;
	private final PriorityBlockingQueue<Point> taskQueue2 = new PriorityBlockingQueue<Point>(11, 
			new Comparator<Point>() {
		public int compare(Point a, Point b) {
			// TODO Auto-generated method stub
			Point playerPosition = PointManager.getCurrentPlayerPosition();
			if (playerPosition == null) {
				return 0;
			}
			a = new Point(a.x * 512 + 256, a.z * 512 + 256);
			b = new Point(b.x * 512 + 256, b.z * 512 + 256);
			
			return a.distance(playerPosition) < b.distance(playerPosition) ? -1 : 1;
		}

	});

	public RegionWriter(PointManager pointManager, Point rootPoint, File regionFolder, 
			GameMetaInfo metaInfo, MappedSquareManager mappedSquareManager, UberDownloader uberDownloader, int verticalExaggeration) {
		super(NUM_WRITING_THREADS);
		this.verticalExaggeration = verticalExaggeration;
		this.rootPoint = rootPoint;
		this.regionFolder = regionFolder;
		this.uberDownloader = uberDownloader;
		this.gameMetaInfo = metaInfo;
		this.mappedSquareManager = mappedSquareManager;
		this.pointManager = pointManager;
	}

	protected Point getTask() throws InterruptedException {
		return taskQueue2.take();
	}
	protected void addTask(Point p) throws InterruptedException {
		if (p != null)
			taskQueue2.add(p);
	}
	public synchronized void addTask(int regionx, int regionz) {
		Point p = new Point(regionx, regionz);
		if (!pointsAdded.contains(p)) {
			pointsAdded.add(p);
			taskQueue2.add(p);
		}
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
		GlobcoverManager coverManager = new GlobcoverManager(regionx, regionz, uberDownloader, verticalExaggeration);

		if (coverManager.allWater) {
			pointManager.updateStore(task);
			mappedSquareManager.addPoint(new Point(task.x + rootPoint.x, task.z + rootPoint.z));
			return;
		}

		RegionFile regionFile = new RegionFile(f);

		//
		for (int x = 0; x < 32; x++) {
			for (int z = 0; z < 32; z++) {
				if (Thread.interrupted()) {
					regionFile.close();
					f.delete();
					throw new InterruptedException();
				}
				Chunk chunk = coverManager.getChunk(x + regionx*32, z + regionz*32, x + a*32, z + b*32);
				DataOutputStream stream = regionFile.getChunkDataOutputStream(x, z);
				NbtIo.write(chunk.getTag(), stream);
				stream.close();
			}
		}
		regionFile.close();


		pointManager.updateStore(task);
		gameMetaInfo.incrementChunksMade();
		mappedSquareManager.addPoint(new Point(task.x + rootPoint.x, task.z + rootPoint.z));
	}

}
