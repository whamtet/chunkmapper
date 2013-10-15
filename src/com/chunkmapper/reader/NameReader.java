package com.chunkmapper.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	public static String getName() {
		return names.get(random.nextInt(names.size()));
	}
}
	