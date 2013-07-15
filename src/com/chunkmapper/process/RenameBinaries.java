package com.chunkmapper.process;

import java.io.File;

public class RenameBinaries {
	
	private static void rename(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				rename(f);
			} else if (f.getName().startsWith("f_")) {
				//be careful here!
				String name = f.getName().split("\\.")[0] + "_.txt";
				f.renameTo(new File(f.getParentFile(), name));
			}
		}
	}
	public static void main(String[] args) throws Exception {
		File a = new File("/Users/matthewmolloy/Downloads/osmosis-master/output");
		rename(a);
	}

}
