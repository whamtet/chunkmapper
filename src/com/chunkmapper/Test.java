package com.chunkmapper;

import java.io.File;


public class Test {

	private static class Rig extends Thread {
		public void run() {
			while(true) {
				System.out.println("hi");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/**
	 * @param args
	 */


}
