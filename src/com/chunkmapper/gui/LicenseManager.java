package com.chunkmapper.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.chunkmapper.Utila;
import com.chunkmapper.gui.ApplicationTemplate.AppFrame;
import com.chunkmapper.gui.dialog.LicenseDialog;

public class LicenseManager {
	public static final String VERSION = "beta";
	public static final File LICENSE_FILE = new File(Utila.CACHE, "license.txt");
	private static String getLicenseVersion() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(LICENSE_FILE));
			String licenseVersion = br.readLine();
			br.close();
			return licenseVersion;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void checkLicense(AppFrame frame) {
		if (!VERSION.equals(getLicenseVersion())) {
			LicenseDialog d = new LicenseDialog(frame);
			d.setVisible(true);
			if (d.licenseAccepted) {
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(LICENSE_FILE)));
					pw.print(VERSION);
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.exit(0);
			}
		}
	}
	public static void main(String[] args) throws Exception {
		checkLicense(null);
		System.out.println("done");
	}

}
