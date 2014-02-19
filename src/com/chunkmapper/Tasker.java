package com.chunkmapper;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


public abstract class Tasker {
	private final ExecutorService executorService;
	private LinkedBlockingQueue<Point> taskQueue = new LinkedBlockingQueue<Point>();
	protected final HashSet<Point> pointsAdded = new HashSet<Point>();

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
		executorService = Executors.newFixedThreadPool(numThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable arg0) {
				Thread t = new Thread(arg0);
				t.setPriority(Thread.MIN_PRIORITY);
				// TODO Auto-generated method stub
				return t;
			}

		});
		for (int i = 0; i < numThreads; i++) {
			executorService.execute(new Runnable() {
				public void run() {

					while(true) {
						Point task = null;
						try {
							task = getTask();
							doTask(task);
						} catch (InterruptedException e) {
							e.printStackTrace();
							return;
						} catch (UnknownHostException e) {
							ManagingThread.setNetworkProblems();
							e.printStackTrace();
							try {
								Thread.sleep(1000);
								addTask(task);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								return;
							}
						} catch (SocketException e) {
							ManagingThread.setNetworkProblems();
							e.printStackTrace();
							try {
								Thread.sleep(1000);
								addTask(task);
							} catch (InterruptedException e1) {
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
