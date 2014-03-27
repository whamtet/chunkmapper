package com.chunkmapper.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
	public static final Logger LOGGER = Logger.getLogger("com.chunkmapper");
	
	static {
		LOGGER.addHandler(FeedbackManager.streamHandler);
//		try {
//			FileHandler h = new FileHandler("logging.txt");
//			h.setFormatter(new SimpleFormatter());
//			LOGGER.addHandler(h);
//			
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public static void deleteLog() {
		(new File("logging.txt")).delete();
	}
	
	public static String printException(Throwable t) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(b);
		t.printStackTrace(s);
		return b.toString();
	}
	public static void throwException() throws Exception {
		throw new Exception();
	}
	public static void main(String[] args) {
		try {
			(new MyLogger()).throwException();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(printException(e));
		}
	}

}
