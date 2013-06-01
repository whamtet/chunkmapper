package com.chunkmapper;

import javax.swing.JProgressBar;

public class ProgressManager {
	private final JProgressBar progressBar;
	private int totalTasks = 0, numTasksDone = 0;

//	public ProgressManager(JProgressBar progressBar) {
//		this.progressBar = progressBar;
//	}
	public ProgressManager(JProgressBar progressBar, int totalTasks) {
		this.totalTasks = totalTasks;
		this.progressBar = progressBar;
	}
	
	public synchronized void incrementTotalTasks() {
		totalTasks++;
	}
	public synchronized void incrementProgress() {
		numTasksDone++;
		int j;
		if (totalTasks < 1) {
			j = 0;
		} else {
			j = 100 * numTasksDone / totalTasks;
			if (j > 100)
				j = 100;
		}
		progressBar.setValue(j);
	}

}
