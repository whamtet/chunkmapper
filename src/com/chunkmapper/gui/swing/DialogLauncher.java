package com.chunkmapper.gui.swing;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;


public class DialogLauncher {
	
	public static void launch(double lat, double lon, JFrame appFrame) {
		//first we need to get minecraft location
		String os = System.getProperty("os.name").toLowerCase();
		File minecraftDir;
		if (os.indexOf("win") >= 0) {
			minecraftDir = new File(FileUtils.getUserDirectory(), "\\.minecraft");
		} else if (os.indexOf("mac") >= 0) {
			minecraftDir = new File(FileUtils.getUserDirectory(), "/Library/Application Support/minecraft");
//			minecraftDir = new File("wwffd");
		} else {
			//linux
			minecraftDir = new File(FileUtils.getUserDirectory(), "/.minecraft");
		}
		if (!minecraftDir.exists()) {
			//we have a problem.  Lets solve it.
			NoMinecraftDialog dialog = new NoMinecraftDialog(lat, lon, appFrame);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);	
		} else {
			MapMakingDialog d = new MapMakingDialog(lat, lon, minecraftDir, appFrame);
			d.setVisible(true);
		}
	}

	public static void main(String[] args) {
//		System.out.println(System.getProperty("file.separator"));
		launch(0, 0, null);
	}
}
