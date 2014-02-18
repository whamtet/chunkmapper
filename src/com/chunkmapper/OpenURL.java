package com.chunkmapper;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenURL {
	public static void main(String[] args) throws URISyntaxException {
		String s = "http://api.openstreetmap.fr/" + "xapi?way[railway=rail%spreserved][bbox=%s,%s,%s,%s]";
		s = String.format(s, "%7C", 2, 48.6, 3, 49.6);
		System.out.println(s);
		openWebpage(new URI(s));
	}
	public static void openWebpage(String s) {
		try {
			openWebpage(new URI(s));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
