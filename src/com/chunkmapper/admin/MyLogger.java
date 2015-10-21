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
	
//	public static void printGraphicsCard() {
//		LOGGER.info(Utila.OS_TYPE.toString());
//		if (Utila.OS_TYPE == OSType.WIN) {
//			try {
//		        String filePath = "foo.txt";
//		        // Use "dxdiag /t" variant to redirect output to a given file
//		        ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c","dxdiag","/t",filePath);
//		        Process p = pb.start();
//		        p.waitFor();
//
//		        BufferedReader br = new BufferedReader(new FileReader(filePath));
//		        String line;
//		        StringBuilder sb = new StringBuilder();
//		        while((line = br.readLine()) != null){
//		            if(line.trim().startsWith("Card name:") || line.trim().startsWith("Current Mode:")){
//		            	sb.append(line.trim() + "\n");
//		            }
//		        }
//		        br.close();
//		        (new File(filePath)).delete();
//		        
//		        LOGGER.info(sb.toString());
//			} catch (InterruptedException ex) {
//				LOGGER.warning(MyLogger.printException(ex));
//		    } catch (IOException ex) {
//		        LOGGER.warning(MyLogger.printException(ex));
//		    }
//		}
//	}
	public static enum SpecialLog { CONTROLS(0), GLOBE(1);
		public final int val;
		private SpecialLog(int val) {
			this.val = val;
		}
	}
	
	public static void specialLog(SpecialLog l) {
		if (!specialFlags[l.val]) {
			LOGGER.info(l.toString());
			specialFlags[l.val] = true;
		}
	}
	public static void init() {}

	static {
		
		LOGGER.addHandler(FeedbackManager.streamHandler);
		if (Utila.isMatt()) {
			try {
				FileHandler h = new FileHandler("logging.txt");
				h.setFormatter(new SimpleFormatter());
				LOGGER.addHandler(h);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.setLevel(Level.CONFIG);
		} else {
			LOGGER.setLevel(Level.INFO);
		}
		LOGGER.info(LOGGER.getLevel().toString());
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
		System.out.println(LOGGER.getLevel());
	}

}
