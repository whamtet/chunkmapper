package com.chunkmapper.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MinecraftInstallFileFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
//		File minecraftJar = new File(arg0, "bin/minecraft.jar");
//		return minecraftJar.exists();
		return true;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Minecraft Directory";
	}

}