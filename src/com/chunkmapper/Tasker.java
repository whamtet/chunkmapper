package com.chunkmapper;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.chunkmapper.admin.MyLogger;


public abstract class Tasker {
	/*
	 * A wrapper around ExecutorService where each task is mapped to a (regionX, regionZ) point.
	 * The caller adds tasks by simply adding points.
	 */
	
	private final ExecutorService executorService;
	protected final HashSet<Point> pointsAdded = new HashSet<Point>();

	public synchronized void addTask(int regionx, int regionz) {
		Point p = new Point(regionx, regionz);
		if (!pointsAdded.contains(p)) {
			pointsAdded.add(p);
			executorService.execute(wrapRunnable(p));
		}
	}


	public Tasker(int numThreads, final String threadName) {
		executorService = Executors.newFixedThreadPool(numThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable arg0) {
				Thread t = new Thread(arg0);
				// TODO Auto-generated method stub
				t.setName(threadName);
				return t;
			}

		});
	}

	private Runnable wrapRunnable(Point p) {
		return () -> {
			while(true) {
				try {
					doTask(p);
					return;
				} catch (InterruptedException e) {
					MyLogger.LOGGER.info("Interrupted while doing task");
					MyLogger.LOGGER.info(MyLogger.printException(e));
					return;
				} catch (UnknownHostException | SocketException e) {
					ManagingThread.setNetworkProblems();
					MyLogger.LOGGER.warning(MyLogger.printException(e));
					try {
						Thread.sleep(1000);
						continue;
					} catch (InterruptedException e1) {
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} catch (Exception e) {
					MyLogger.LOGGER.warning(MyLogger.printException(e));
					try {
						Thread.sleep(1000);
						continue;
					} catch (InterruptedException e1) {
						MyLogger.LOGGER.info("Interrupted Sleep");
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} catch (Error e) {
					MyLogger.LOGGER.severe(MyLogger.printException(e));
				}
			}
		};
	}

	protected abstract void doTask(Point p) throws Exception;


}
