package com.chunkmapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.admin.MyLogger;

public class PostingThread extends Thread {
	private HashSet<Point> posted = new HashSet<Point>();
	private final File regionDir, store;
	private final Point rootPoint;
	public PostingThread(File gameDir, Point rootPoint) throws NumberFormatException, IOException {
		regionDir = new File(gameDir, "region");
		this.rootPoint = rootPoint;
		store = new File(gameDir, "chunkmapper/posted.txt");
		if (store.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(store));
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				int regionx = Integer.parseInt(split[0]);
				int regionz = Integer.parseInt(split[1]);
				posted.add(new Point(regionx, regionz));
			}
			br.close();
		}
	}
	private class FilePoint {
		public final File f;
		public final Point p;
		public FilePoint(File f) {
			this.f = f;
			String[] split = f.getName().split("\\.");
			int relx = Integer.parseInt(split[1]);
			int relz = Integer.parseInt(split[1]);
			p = new Point(rootPoint.x + relx, rootPoint.z + relz);
		}
	}
	private ArrayList<FilePoint> getTodo() {
		ArrayList<FilePoint> out = new ArrayList<FilePoint>();

		for (File f : regionDir.listFiles()) {
			if (f.getName().endsWith(".mca")) {
				FilePoint fp = new FilePoint(f);
				if (!posted.contains(fp.p)) {
					out.add(fp);
					posted.add(fp.p);
				}
			}
		}

		return out;
	}
	@Override
	public void run() {
		while (true) {
			ArrayList<FilePoint> todo;
			while(true) {
				todo = getTodo();
				if (todo.isEmpty()) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} else {
					break;
				}
			}
			for (FilePoint fp : todo) {
				if (FileValidator.checkSupervalid(fp.f)) {
					post(fp);
				}
				try {
					spit();
				} catch (IOException e) {
					MyLogger.LOGGER.info(MyLogger.printException(e));
				}
			}
		}
	}
	private void spit() throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(store)));
		for (Point p : posted) {
			pw.println(p.x + " " + p.z);
		}
		pw.close();
	}
	private void post(FilePoint fp) {
		
	}
}

