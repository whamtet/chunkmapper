package com.chunkmapper.admin;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class FeedbackManager {
	public static final ByteArrayOutputStream loggingStream = new ByteArrayOutputStream();
	public static final StreamHandler streamHandler = new StreamHandler(loggingStream, new SimpleFormatter());
	
	public static void main(String[] args) {
		
	}
		
	public static class FeedbackHandler extends Handler {
		
		public List<String> lines = Collections.synchronizedList(new ArrayList<String>());

		@Override
		public void publish(LogRecord record) {
			// TODO Auto-generated method stub
			lines.add(record.getMessage());
			record.
		}

		@Override
		public void flush() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void close() throws SecurityException {
			// TODO Auto-generated method stub
			
		}
		
	};
	static {
		
	}
	
}
