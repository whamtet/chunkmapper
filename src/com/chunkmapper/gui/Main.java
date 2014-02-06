/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package com.chunkmapper.gui;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.Earth.MSVirtualEarthLayer;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.util.HotSpotController;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.chunkmapper.Utila;
import com.chunkmapper.interfaces.GlobalSettings;
import com.chunkmapper.layer.MCNotAvailableLayer;
import com.chunkmapper.layer.MainLayer;

/**
 * Example of using {@link gov.nasa.worldwind.util.tree.BasicTree} to display a list of layers.
 *
 * @author pabercrombie
 * @version $Id: LayerTreeUsage.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Main extends ApplicationTemplate
{
	private static Image getIcon() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage(Main.class.getResource("/images/ChunkmapperIcon.png"));
		return img;
	}
	public static class AppFrame extends ApplicationTemplate.AppFrame
	{
		//        protected LayerTree layerTree;
		protected RenderableLayer hiddenLayer;

		protected HotSpotController controller;

		public AppFrame() throws IOException
		{
			super(true, false, false); // Don't include the layer panel; we're using the on-screen layer tree.
			
			//add our own layer
			getWwd().getModel().getLayers().add(new MSVirtualEarthLayer());
			
			Dimension size = new Dimension(1000, 600);
			this.setPreferredSize(size);
			this.pack();
			WWUtil.alignComponent(null, this, AVKey.CENTER);
			
			File minecraftDir = Utila.MINECRAFT_DIR;
			if (!minecraftDir.exists()) {
				getWwd().getModel().getLayers().add(new MCNotAvailableLayer(this.getWwd(), this, globalSettings));
			} else {
				addMainLayer(this.getWwd(), minecraftDir, this, globalSettings);
			}
//			this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/ChunkmapperIcon.png")).getImage());
			
		}

		
	}
	public static void addMainLayer(WorldWindow wwd, File minecraftDir, JFrame appFrame, GlobalSettings globalSettings) throws IOException {
		wwd.getModel().getLayers().add(new MainLayer(wwd, appFrame, minecraftDir, globalSettings));
	}
	
	private static boolean hasFlawed(String[] args) {
		for (String arg : args) {
			if (arg.equals("-flawed"))
				return true;
		}
		return false;
	}

	public static void main(String[] args)
	{
		if (hasFlawed(args))
			Utila.MINECRAFT_DIR = new File("poo");
		long availableMemory = Runtime.getRuntime().maxMemory();
		if (availableMemory < 1000000000)
			System.err.println("Warning: Xmx set too low: " + availableMemory);
//		JOptionPane.showMessageDialog(null, Utila.MINECRAFT_DIR.toString());
		ApplicationTemplate.start("Chunkmapper", AppFrame.class);
	}
}
