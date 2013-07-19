package com.chunkmapper.process;

import java.io.File;

public class RenameTrees {
	
	public static void main(String[] args) {
		File f = new File("/Users/matthewmolloy/Downloads/ww/src/images/trees2");
		for (File g : f.listFiles()) {
			g.renameTo(new File(g.getParentFile(), g.getName() + ".myschematic"));
		}
	}

}
