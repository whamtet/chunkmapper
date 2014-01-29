package com.chunkmapper.process;

import java.io.File;
import java.io.IOException;

public class MavenizeDeps {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f = new File("/Users/matthewmolloy/Downloads/ww2");
		for (File g : f.listFiles()) {
			if (g.getName().endsWith(".jar") && !g.getName().startsWith("w") && !g.getName().startsWith("v")) {
				String name = g.getName().split("\\.")[0];
				System.out.println(name);
//				String s = String.format("mvn install:install-file -Dfile=%s -DartifactId=%s -DgroupId=self -Dversion=1.0 -Dpackaging=jar",
//						g.getAbsolutePath(), name);
//				Runtime.getRuntime().exec(s);
//				
//				System.out.println("<dependency>");
//				System.out.println("<groupId>self</groupId>");
//				System.out.println("<artifactId>" + name + "</artifactId>");
//				System.out.println("<version>1.0</version>");
//				System.out.println("</dependency>");
			}
		}

	}

}
