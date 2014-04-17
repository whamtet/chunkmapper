package com.chunkmapper.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import com.chunkmapper.admin.MyLogger;

public class NameReader {
	private static Random random = new Random();
	private static ArrayList<String> names = new ArrayList<String>();
	static {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(NameReader.class.getResourceAsStream("/names.txt")));
			String line;
			while((line = br.readLine()) != null) {
				names.add(line);
			}
			br.close();
			
			br = new BufferedReader(new InputStreamReader(NameReader.class.getResourceAsStream("/maori-names.txt")));
			
			while ((line = br.readLine()) != null) {
				names.add(line);
			}
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	public static String getName() {
		return names.get(random.nextInt(names.size()));
	}
	public static void main(String[] args) throws Exception {
		System.out.println(getName());
	}
}
	