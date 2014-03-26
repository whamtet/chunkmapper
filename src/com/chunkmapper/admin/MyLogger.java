package com.chunkmapper.admin;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class MyLogger {
	public static final Logger LOGGER = Logger.getLogger("com.chunkmapper");

	static {
		LOGGER.addHandler(FeedbackManager.streamHandler);
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
