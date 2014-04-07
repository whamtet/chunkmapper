package com.chunkmapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;

import com.chunkmapper.admin.MyLogger;

public class LoggingBufferedReader extends BufferedReader {
	
	private final Level level;
	public LoggingBufferedReader(Reader r, Level l) {
		super(r);
		level = l;
	}
	@Override
	public String readLine() {
		try {
			return super.readLine();
		} catch (IOException e) {
			MyLogger.LOGGER.log(level, MyLogger.printException(e));
		}
		return null;
	}
	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
			MyLogger.LOGGER.log(level, MyLogger.printException(e));
		}
	}

}
