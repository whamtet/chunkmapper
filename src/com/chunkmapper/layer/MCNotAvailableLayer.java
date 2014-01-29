/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package com.chunkmapper.layer;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.util.Logging;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.chunkmapper.gui.Main;
import com.chunkmapper.gui.MinecraftInstallFileFilter;
import com.chunkmapper.interfaces.GlobalSettings;

public class MCNotAvailableLayer extends RenderableLayer implements SelectListener
{
	protected WorldWindow wwd;
	protected boolean update = true;

	private ScreenAnnotation parentAnnotation, locateMinecraftButton;
	protected Dimension size;
	private int selectedIndex = -1;
	private Color color = Color.decode("#b0b0b0");
	private Color highlightColor = Color.decode("#ffffff");
	private double minOpacity = .6;
	private double maxOpacity = 1;
	private char layerEnabledSymbol = '\u25a0';
	private char layerDisabledSymbol = '\u25a1';
	private Font font = new Font("SansSerif", Font.PLAIN, 14);
	private boolean minimized = false;
	private int borderWidth = 20; // TODO: make configurable
	private String position = AVKey.SOUTHWEST; // TODO: make configurable
	private Vec4 locationCenter = null;
	private Vec4 locationOffset = null;

	private final WorldMapLayer worldMapLayer;
	private final JFrame appFrame;

	private final GlobalSettings globalSettings;
	public MCNotAvailableLayer(WorldWindow wwd, JFrame appFrame, GlobalSettings globalSettings)
	{
		this.globalSettings = globalSettings;
		this.appFrame = appFrame;
		LayerList ll = wwd.getModel().getLayers();
		WorldMapLayer worldMapLayer = null;

		for (int i = 0; i < ll.size(); i++) {
			Layer l = ll.get(i);
			if (l instanceof WorldMapLayer)
				worldMapLayer = (WorldMapLayer) l;
		}
		this.worldMapLayer = worldMapLayer;

		this.wwd = wwd;

		// Set up screen annotation that will display the layer list
		this.parentAnnotation = new ScreenAnnotation("", new Point(0, 0));

		// Set annotation so that it will not force text to wrap (large width) and will adjust it's width to
		// that of the text. A height of zero will have the annotation height follow that of the text too.
		this.parentAnnotation.getAttributes().setSize(new Dimension(Integer.MAX_VALUE, 0));
		this.parentAnnotation.getAttributes().setAdjustWidthToText(AVKey.SIZE_FIT_TEXT);

		// Set appearance attributes
		this.parentAnnotation.getAttributes().setCornerRadius(0);
		this.parentAnnotation.getAttributes().setFont(this.font);
		this.parentAnnotation.getAttributes().setHighlightScale(1);
		this.parentAnnotation.getAttributes().setTextColor(Color.WHITE);
		this.parentAnnotation.getAttributes().setBackgroundColor(new Color(0f, 0f, 0f, .5f));
		this.parentAnnotation.getAttributes().setInsets(new Insets(6, 6, 6, 6));
		this.parentAnnotation.getAttributes().setBorderWidth(1);

		//        locateMinecraftButton = new ScreenAnnotation("", new Point(0, 0));
		//        locateMinecraftButton.getAttributes().setCornerRadius(0);
		//        locateMinecraftButton.getAttributes().setFont(this.font);
		//        locateMinecraftButton.getAttributes().setTextColor(Color.WHITE);
		//        
		//        locateMinecraftButton.setText("Locate Minecraft");
		//        parentAnnotation.addChild(locateMinecraftButton);
		//        
		this.addRenderable(this.parentAnnotation);

		// Listen to world window for select event
		this.wwd.addSelectListener(this);
	}

	public void selected(SelectEvent event) {

		if (event.hasObjects() && event.getTopObject() == this.parentAnnotation)
		{
			boolean update = false;
			if (event.getEventAction().equals(SelectEvent.ROLLOVER)
					|| event.getEventAction().equals(SelectEvent.LEFT_CLICK))
			{
				// Highlight annotation
				if (!this.parentAnnotation.getAttributes().isHighlighted())
				{
					this.parentAnnotation.getAttributes().setHighlighted(true);
					update = true;
				}
				if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
					File f = chooseMinecraftFile(appFrame);
					if (f != null) {
						wwd.getModel().getLayers().remove(this);
						try {
							Main.addMainLayer(wwd, f, appFrame, globalSettings);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if (event.getEventAction().equals(SelectEvent.DRAG)
					|| event.getEventAction().equals(SelectEvent.DRAG_END))
			{
			}
			// Redraw annotation if needed
			if (update)
				this.update();
		}
		else if (event.getEventAction().equals(SelectEvent.ROLLOVER) && this.parentAnnotation.getAttributes().isHighlighted())
		{
			// de-highlight annotation
			this.parentAnnotation.getAttributes().setHighlighted(false);
			((Component) this.wwd).setCursor(Cursor.getDefaultCursor());
			this.update();
		}
	}

	private static File chooseMinecraftFile(JFrame appFrame) {
		JFileChooser fc = new JFileChooser();		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new MinecraftInstallFileFilter());
		fc.setDialogTitle("Select Minecraft Install Location");

		fc.showOpenDialog(appFrame);
		return fc.getSelectedFile();
	}

	/** Schedule the layer list for redrawing before the next render pass. */
	public void update()
	{
		this.update = true;
		this.wwd.redraw();
	}

	/**
	 * Force the layer list to redraw itself from the current <code>Model</code> with the current highlighted state and
	 * selected layer colors and opacity.
	 *
	 * @param dc the current {@link DrawContext}.
	 *
	 * @see #setMinOpacity(double)
	 * @see #setMaxOpacity(double)
	 * @see #setColor(java.awt.Color)
	 * @see #setHighlightColor(java.awt.Color)
	 */
	public void updateNow(DrawContext dc)
	{
		// Adjust annotation appearance to highlighted state
		this.highlight(this.parentAnnotation.getAttributes().isHighlighted());

		// Compose html text
		String text = this.makeAnnotationText(this.wwd.getModel().getLayers());
		this.parentAnnotation.setText(text);

		// Update current size and adjust annotation draw offset according to it's width
		// TODO: handle annotation scaling
		this.size = this.parentAnnotation.getPreferredSize(dc);
		this.parentAnnotation.getAttributes().setDrawOffset(new Point(this.size.width / 2, 0));

		// Clear update flag
		this.update = false;
	}

	/**
	 * Change the annotation appearance according to the given highlighted state.
	 *
	 * @param highlighted <ode>true</code> if the annotation should appear highlighted.
	 */
	protected void highlight(boolean highlighted)
	{
		// Adjust border color and annotation opacity
		if (highlighted)
		{
			this.parentAnnotation.getAttributes().setBorderColor(this.highlightColor);
			this.parentAnnotation.getAttributes().setOpacity(this.maxOpacity);
		}
		else
		{
			this.parentAnnotation.getAttributes().setBorderColor(this.color);
			this.parentAnnotation.getAttributes().setOpacity(this.minOpacity);
		}
	}

	/**
	 * Compose the annotation text from the given <code>LayerList</code>.
	 *
	 * @param layers the <code>LayerList</code> to draw names from.
	 *
	 * @return the annotation text to be displayed.
	 */
	private static String divWidth(int i) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			sb.append("<div> </div>");
		}
		return sb.toString();
	}
	protected String makeAnnotationText(LayerList layers)
	{
		return "<font color=\"#b0b0b0\"><b>Minecraft not Found</b><br /><br />"
				+ divWidth(8) + "Click Here";
	}

	protected static String encodeHTMLColor(Color c)
	{
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}

	public void render(DrawContext dc)
	{
		if (this.update)
			this.updateNow(dc);

		this.parentAnnotation.setScreenPoint(computeLocation(dc.getView().getViewport()));
		super.render(dc);
	}

	protected Point computeLocation(Rectangle viewport)
	{

		int x = 20, y = (int) (viewport.getHeight() - 90 - viewport.getWidth() * .1) - 15;
		return new Point(x, y);
	}

	@Override
	public String toString()
	{
		return Logging.getMessage("layers.LayerManagerLayer.Name");
	}
}
