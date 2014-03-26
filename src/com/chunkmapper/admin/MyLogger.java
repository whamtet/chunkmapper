package com.chunkmapper.admin;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class MyLogger {
	public static final Logger LOGGER = Logger.getLogger("");

	public static void init() {
		LOGGER.addHandler(FeedbackManager.streamHandler);
	}
	public static void main(String[] args) {
		init();
		Logger subLogger = Logger.getLogger(MyLogger.class.getName());
		subLogger.warning("hi");
		FeedbackManager.streamHandler.flush();
		System.out.println(FeedbackManager.loggingStream);
	}

}
