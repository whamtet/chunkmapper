package com.chunkmapper;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.*;

import com.chunkmapper.admin.MyLogger;


public abstract class Tasker {
	/*
	 * A wrapper around ExecutorService where each task is mapped to a (regionX, regionZ) point.
	 * The caller adds tasks by simply adding points.
	 */
	
	private final ExecutorService executorService;
	protected final HashSet<Point> pointsAdded = new HashSet<>();
	private final Comparator<Point> comparator;

	public synchronized void addTask(int regionx, int regionz) {
		Point p = new Point(regionx, regionz);
		if (!pointsAdded.contains(p)) {
			pointsAdded.add(p);
			executorService.execute(new Task(p));
		}
	}

	public Tasker(int numThreads) {
		this(numThreads, (p1, p2) -> 0);
	}
	public Tasker(int numThreads, Comparator<Point> c) {
		comparator = c;
		executorService = new ThreadPoolExecutor(numThreads, numThreads,
				0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
	}

	private class Task implements Runnable, Comparable {
		public final Point p;

		Task(Point p) {
			this.p = p;
		}

		public void run() {
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

		@Override
		public int compareTo(Object o) {
			return comparator.compare(p, ((Task) o).p);
		}
	}

	protected abstract void doTask(Point p) throws Exception;


}
