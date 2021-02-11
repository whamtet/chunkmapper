package com.chunkmapper.admin;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class MyLogger {
	
	/*
	 * Logging Utility Class
	 */
	public static final Logger LOGGER = Logger.getLogger("gov");

	public static String printException(Throwable t) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(b);
		t.printStackTrace(s);
		return b.toString();
	}
}
