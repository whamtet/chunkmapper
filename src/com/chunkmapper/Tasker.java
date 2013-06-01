package com.chunkmapper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.chunkmapper.reader.FileNotYetAvailableException;


public abstract class Tasker {
	private final ExecutorService executorService;
	//	private final ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();
//	protected final HashSet<Point> taskQueue = new HashSet<Point>(), doneAlready = new HashSet<Point>();
	private final LinkedList<Point> taskQueue = new LinkedList<Point>();
	private final HashSet<Point> pointsAdded = new HashSet<Point>();
	protected Object lock = new Object();

	public void shutdown() {
		executorService.shutdownNow();
		System.out.println("shut down " + this.getClass().toString());
	}
	public void blockingShutdown() {
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addTask(int regionx, int regionz) {
		synchronized(lock) {
			Point p = new Point(regionx, regionz);
			if (!pointsAdded.contains(p)) {
				pointsAdded.add(p);
				taskQueue.add(p);
			}
		}
	}


	public Tasker(int numThreads, final PointManager pointManager, final GameMetaInfo metaInfo, final ProgressManager progressManager) {
		executorService = Executors.newFixedThreadPool(numThreads);
		for (int i = 0; i < numThreads; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					while(true) {
						if (Thread.interrupted()) {
							System.err.println("shutting down now");
							return;
						}
						Point task = null;
						synchronized(lock) {
							task = taskQueue.poll();
						}
						if (task == null) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								System.err.println("shutting down now");
								return;
							}
						} else {
							try {
								doTask(task);
								if (pointManager != null) {
									pointManager.updateStore(task);
								}
								if (metaInfo != null) {
									metaInfo.incrementChunksMade();
								}
								if (progressManager != null) {
									progressManager.incrementProgress();
								}
							} catch (InterruptedException e) {
								return;
							} catch (FileNotYetAvailableException e) {
//								e.printStackTrace();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									return;
								}
								synchronized(lock) {
									taskQueue.add(task);
								}

							} catch (Exception e) {
								e.printStackTrace();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									return;
								}
								synchronized(lock) {
									taskQueue.add(task);
								}
							} catch (Error e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
	}
	protected abstract void doTask(Point p) throws Exception;


}
