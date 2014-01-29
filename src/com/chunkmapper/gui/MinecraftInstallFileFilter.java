package com.chunkmapper.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MinecraftInstallFileFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
		
		File binDir = new File(arg0, "bin"), minecraftJar = new File(binDir, "minecraft.jar");
		return minecraftJar.exists();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Minecraft Directory";
	}

}