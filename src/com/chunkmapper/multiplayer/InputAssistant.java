package com.chunkmapper.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputAssistant {
	public static String readLine(String format, Object... args) {
	    if (System.console() != null) {
	        return System.console().readLine(format, args);
	    }
	    System.out.print(String.format(format, args));
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
	            System.in));
	    try {
	    	return reader.readLine();
	    } catch (IOException e) {
	    	return null;
	    }
	}

	public static String readPassword(String format, Object... args)
	        {
	    if (System.console() != null)
	        return new String(System.console().readPassword(format, args));
	    return readLine(format, args);
	}

}
