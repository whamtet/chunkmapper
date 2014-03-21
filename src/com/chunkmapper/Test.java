package com.chunkmapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws Exception {
		
		Pattern p = Pattern.compile("<version>(.*)</version>");
		Matcher m = p.matcher("<version>hihi</version>");
		if (m.find()) {
			System.out.println(m.group(1));
		}
	}
}
