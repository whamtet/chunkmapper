package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.chunkmapper.admin.Utila.OSType;

public class MyLogger {
	
	/*
	 * Logging Utility Class
	 */
	public static final Logger LOGGER = Logger.getLogger("gov");
	private static final boolean[] specialFlags = new boolean[SpecialLog.values().length];

	public static enum SpecialLog { CONTROLS(0), GLOBE(1);
		public final int val;
		private SpecialLog(int val) {
			this.val = val;
		}
	}


	public static String printException(Throwable t) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(b);
		t.printStackTrace(s);
		return b.toString();
	}
}
