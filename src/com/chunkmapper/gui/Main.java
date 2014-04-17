/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package com.chunkmapper.gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.Earth.MSVirtualEarthLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.util.HotSpotController;
import gov.nasa.worldwindx.examples.util.ShapeUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.PreferenceManager;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.gui.bounds.MyPolygonEditor;
import com.chunkmapper.gui.dialog.UpgradeAvailableDialog;
import com.chunkmapper.gui.layer.MCNotAvailableLayer;
import com.chunkmapper.gui.layer.MainLayer;
import com.chunkmapper.security.MySecurityManager;

/**
 * Example of using {@link gov.nasa.worldwind.util.tree.BasicTree} to display a list of layers.
 *
 * @author pabercrombie
 * @version $Id: LayerTreeUsage.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Main extends ApplicationTemplate
{
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

			//listener to notify that globe has been moved
			final View v = getWwd().getView();
			v.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					MyLogger.LOGGER.info("Globe Moved");
					v.removePropertyChangeListener(this);
				}});


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
			this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
			this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Globe.png")).getImage());

		}

		public static void addTestPath(WorldWindow wwd) {
			RenderableLayer l = new RenderableLayer();
			
			ExtrudedPolygon extrudedPolygon = ExtrudedPolygonFactory.createPolygon(wwd);
			extrudedPolygon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			l.addRenderable(extrudedPolygon);
			MyPolygonEditor editor = new MyPolygonEditor();
			editor.setWorldWindow(wwd);
			editor.setPolygon(extrudedPolygon);
			editor.setArmed(true);
			ApplicationTemplate.insertBeforeCompass(wwd, editor);
			ApplicationTemplate.insertBeforeCompass(wwd, l);
			System.out.println(extrudedPolygon);
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
//	private static void printUser() {
//		String initLog = PreferenceManager.getInitLog();
//		if (initLog == null) {
//			try {
//				BufferedReader br = new BufferedReader(new InputStreamReader((new URL(BucketInfo.getLogUserUrl())).openStream()));
//				initLog = br.readLine();
//				PreferenceManager.setInitLog(initLog);
//				MyLogger.LOGGER.info(initLog);
//				br.close();
//			} catch (IOException e) {
//				MyLogger.LOGGER.warning(MyLogger.printException(e));
//			}
//		} else {
//			MyLogger.LOGGER.info(initLog);
//		}
//	}
	private static void getBucket() {
		if (!BucketInfo.initMap()) {
			JOptionPane.showMessageDialog(null, "Chunkmapper could not connect with the internet.  Please check your connection and try again.");
			System.exit(0);
		}
	}
	private static void printBuild() {
		try {
			URL url = Main.class.getResource("/build-no.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			MyLogger.LOGGER.info("build-no: " + br.readLine());
			MyLogger.LOGGER.info("verion: " + Utila.VERSION);
			br.close();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}

	public static void main(String[] args) {
		printBuild();
		getBucket();
//		printUser();
		if (BucketInfo.mustUpgrade()) {
			JOptionPane.showMessageDialog(null, "This version of Chunkmapper is no longer supported.  Visit www.chunkmapper.com to upgrade.");
			System.exit(0);
		}
		if (!PreferenceManager.getIgnoreUpgrade() && BucketInfo.spUpgradeAvailable()) {
			(new UpgradeAvailableDialog(null)).setVisible(true);
		}
		LicenseManager.checkLicense(null);
		if (hasFlawed(args))
			Utila.MINECRAFT_DIR = new File("poo");
		long availableMemory = Runtime.getRuntime().maxMemory();
		if (availableMemory < 1000000000)
			System.err.println("Warning: Xmx set too low: " + availableMemory);
		String title = MySecurityManager.isOfflineValid() ? "Chunkmapper" : "Chunkmapper - Free Version";
		ApplicationTemplate.start(title, AppFrame.class);
	}
	
	protected static class ExtrudedPolygonFactory
    {
        public ExtrudedPolygonFactory()
        {
        }

        public static ExtrudedPolygon createPolygon(WorldWindow wwd)
        {
        	boolean fitShapeToViewport = true;
            ExtrudedPolygon poly = new ExtrudedPolygon();
            poly.setAttributes(getDefaultAttributes());
            poly.setValue(AVKey.DISPLAY_NAME, "poo poo");
            initializePolygon(wwd, poly, fitShapeToViewport);

            return poly;
        }
        protected static final double DEFAULT_SHAPE_SIZE_METERS = 200000.0; // 200 km

        protected static void initializePolygon(WorldWindow wwd, ExtrudedPolygon polygon, boolean fitShapeToViewport)
        {
            // Creates a rectangle in the center of the viewport. Attempts to guess at a reasonable size and height.

            Position position = ShapeUtils.getNewShapePosition(wwd);
            Angle heading = ShapeUtils.getNewShapeHeading(wwd, true);
            double heightInMeters = fitShapeToViewport ?
                ShapeUtils.getViewportScaleFactor(wwd) : DEFAULT_SHAPE_SIZE_METERS;

            java.util.List<Position> locations = ShapeUtils.createPositionSquareInViewport(wwd, position, heading,
                heightInMeters);

            polygon.setOuterBoundary(locations);
            polygon.setHeight(heightInMeters);
            polygon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        }

        public static ShapeAttributes getDefaultAttributes()
        {
            ShapeAttributes attributes = new BasicShapeAttributes();
            attributes.setInteriorMaterial(new Material(Color.BLACK, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, 0.0f));
            attributes.setOutlineMaterial(Material.DARK_GRAY);
            attributes.setDrawOutline(true);
            attributes.setInteriorOpacity(0.95);
            attributes.setOutlineOpacity(.95);
            attributes.setOutlineWidth(2);
            return attributes;
        }
    }
}
