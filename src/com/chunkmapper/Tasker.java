package com.chunkmapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.chunkmapper.reader.FileNotYetAvailableException;


public abstract class Tasker {
	private final ExecutorService executorService;
	private LinkedBlockingQueue<Point> taskQueue = new LinkedBlockingQueue<Point>();
	protected final HashSet<Point> pointsAdded = new HashSet<Point>();
	private boolean shutdown = false;

	public void shutdownNow() {
		executorService.shutdownNow();
		System.err.println("shut down " + this.getClass().toString());
	}
	public void blockingShutdownNow() {
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("shut down " + this.getClass().toString());
	}
	public synchronized void addTask(int regionx, int regionz) {
		Point p = new Point(regionx, regionz);
		if (!pointsAdded.contains(p)) {
			pointsAdded.add(p);
			taskQueue.add(p);
		}
	}
	protected Point getTask() throws InterruptedException {
		return taskQueue.take();
	}
	protected void addTask(Point p) throws InterruptedException {
		taskQueue.add(p);
	}


	public Tasker(int numThreads) {
		executorService = Executors.newFixedThreadPool(numThreads);
		for (int i = 0; i < numThreads; i++) {
			executorService.execute(new Runnable() {
				public void run() {

					while(true) {
						Point task = null;
						try {
							task = getTask();
							if (task == null)
								throw new RuntimeException("impossible");
							doTask(task);

						} catch (InterruptedException e) {
							return;
						} catch (FileNotYetAvailableException e) {
							e.printStackTrace();
							try {
								Thread.sleep(1000);
								addTask(task);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								return;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							try {
								Thread.sleep(1000);
								addTask(task);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								return;
							}

						} catch (Error e) {
							e.printStackTrace();
						}
					}
				}
		});
	}
}
protected abstract void doTask(Point p) throws Exception;


}
