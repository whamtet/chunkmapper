package com.chunkmapper;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import com.chunkmapper.admin.MyLogger;


public abstract class Tasker {
	/*
	 * A wrapper around ExecutorService where each task is mapped to a (regionX, regionZ) point.
	 * The caller adds tasks by simply adding points.
	 */
	
	private final ExecutorService executorService;
	private LinkedBlockingQueue<Point> taskQueue = new LinkedBlockingQueue<Point>();
	protected final HashSet<Point> pointsAdded = new HashSet<Point>();

	public void shutdownNow() {
		executorService.shutdownNow();
		MyLogger.LOGGER.info("shut down " + this.getClass().toString());
	}
	public void blockingShutdownNow() {
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			MyLogger.LOGGER.info(MyLogger.printException(e));
		}
		MyLogger.LOGGER.info("shut down " + this.getClass().toString());
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


	public Tasker(int numThreads, final String threadName) {
		executorService = Executors.newFixedThreadPool(numThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable arg0) {
				Thread t = new Thread(arg0);
				//				t.setPriority(Thread.MIN_PRIORITY);
				// TODO Auto-generated method stub
				t.setName(threadName);
				return t;
			}

		});
		for (int i = 0; i < numThreads; i++) {
			executorService.execute(new Task(i));
		}
	}
	private class Task implements Runnable {
		
		private final int i;
		public Task(int i) {
			this.i = i;
		}

		public void run() {

			while(true) {
				Point task = null;
				try {
					task = getTask();
					doTask(task);
				} catch (InterruptedException e) {
					MyLogger.LOGGER.info("Interrupted while doing task");
					MyLogger.LOGGER.info(MyLogger.printException(e));
//					JOptionPane.showMessageDialog(null, "Interrupted while doing task " + i);
					return;
				} catch (UnknownHostException e) {
					ManagingThread.setNetworkProblems();
					MyLogger.LOGGER.warning(MyLogger.printException(e));
					try {
						Thread.sleep(1000);
						addTask(task);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} catch (SocketException e) {
					ManagingThread.setNetworkProblems();
					MyLogger.LOGGER.warning(MyLogger.printException(e));
					try {
						Thread.sleep(1000);
						addTask(task);
					} catch (InterruptedException e1) {
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} catch (Exception e) {
					MyLogger.LOGGER.warning(MyLogger.printException(e));
					try {
						Thread.sleep(1000);
						addTask(task); 
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						MyLogger.LOGGER.info("Interrupted Sleep");
//						JOptionPane.showMessageDialog(null, "Interrupted Sleep " + i);
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}

				} catch (Error e) {
					MyLogger.LOGGER.severe(MyLogger.printException(e));
					MyLogger.LOGGER.severe(task.toString());
				}
			}
		}
	}


	protected abstract void doTask(Point p) throws Exception;


}
