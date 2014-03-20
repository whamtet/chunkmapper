/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package com.chunkmapper.layer;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.util.Logging;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.gui.GoToThread;
import com.chunkmapper.gui.dialog.GoToDialog;
import com.chunkmapper.gui.dialog.MustUpgradeDialog;
import com.chunkmapper.gui.dialog.NewMapDialog;
import com.chunkmapper.interfaces.GlobalSettings;

public class MainLayer extends RenderableLayer implements SelectListener
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
	private final File savesDir;
	private int numRows = 0;
	public HashSet<String> takenGames, newlyCreatedGames = new HashSet<String>();

	private static final int HAND_CURSOR = 0, DEFAULT_CURSOR = 1;
	private final GlobalSettings globalSettings;
	
	public static void main(String[] args) throws Exception {
		String message = "Delete game foo?";
		System.out.println(JOptionPane.showConfirmDialog(null, message, "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE));
	}

	public MainLayer(WorldWindow wwd, final JFrame appFrame, File minecraftDir, GlobalSettings globalSettings) throws IOException
	{

		this.globalSettings = globalSettings;
		savesDir = new File(minecraftDir, "saves");
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

		this.addRenderable(this.parentAnnotation);

		// Listen to world window for select event
		this.wwd.addSelectListener(this);

		wwd.getInputHandler().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {
				switch (arg0.getKeyChar()) {
				case 'g':
					displayGotoDialog();
					break;
				case 'z':
					zoomOut();
					break;
				}
			}			
		});
	}
	private void zoomOut() {
		final View v = wwd.getView();
		final Position p = v.getCurrentEyePosition();
		if (p.elevation < 1.9e7) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					v.goTo(p, 2e7);
					while (v.isAnimating()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {}
					}
					JOptionPane.showMessageDialog(appFrame, "Zoomed out.");
				}
			});
			t.start();
		} else {
			JOptionPane.showMessageDialog(appFrame, "Zoomed out.");
		}
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
				PickedObject po = event.getTopPickedObject();
				if (po != null) {
					String s = (String) po.getValue(AVKey.URL);
					if (s != null) {
						((Component) this.wwd).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
							if (s.equals("create") && !mustUpgrade()) {
								NewMapDialog dialog = new NewMapDialog(this, this.appFrame);
								dialog.setVisible(true);
								String gameName = dialog.getGameName();
								if (gameName != null) {
									this.newlyCreatedGames.add(gameName);
									update = true;

									wwd.getModel().getLayers().remove(this);
									try {
										wwd.getModel().getLayers().add(new GeneratingLayerImpl(wwd, appFrame, savesDir, gameName, this, globalSettings));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							if (s.endsWith("Delete")) {
								String gameName = s.substring(0, s.length() - 6);
//								ConfirmDeleteDialog d = new ConfirmDeleteDialog(appFrame, gameName, savesDir);
//								d.setVisible(true);
								String message = "Delete " + gameName + "?";
								int delete = JOptionPane.showConfirmDialog(appFrame, message, "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
								if (delete == JOptionPane.OK_OPTION) {
									File f = new File(savesDir, gameName);
									try {
										FileUtils.deleteDirectory(f);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									newlyCreatedGames.remove(gameName);
									update = true;
								}
							}
							if (s.endsWith("Resume") && !mustUpgrade()) {
								wwd.getModel().getLayers().remove(this);
								String gameName = s.substring(0, s.length() - 6);
								try {
									wwd.getModel().getLayers().add(new GeneratingLayerImpl(wwd, appFrame, savesDir, gameName, this, globalSettings));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (s.equals("goto")) {
								displayGotoDialog();
							}
						}
					} else {
						((Component) this.wwd).setCursor(Cursor.getDefaultCursor());
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
	private static boolean mustUpgrade() {
		boolean mustUpgrade = BucketInfo.mustUpgrade();
		if (mustUpgrade) {
			MustUpgradeDialog d = new MustUpgradeDialog();
			d.setModalityType(ModalityType.APPLICATION_MODAL);
			d.setVisible(true);
		}
		return mustUpgrade;
	}

	public void displayGotoDialog() {
		GoToDialog d = new GoToDialog(appFrame);
		d.setVisible(true);
		String location = d.getPlace();
		if (location != null) {
			GoToThread t = new GoToThread(appFrame, wwd, location);
			t.start();
		}

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
		this.takenGames = new HashSet<String>();
		HashSet<String> gamesToDisplay = new HashSet<String>();
		for (String s : newlyCreatedGames) {
			takenGames.add(s);
			gamesToDisplay.add(s);
		}
		for (File f : savesDir.listFiles()) {
			//			try {
			File loadedLevelDatFile = new File(f, "level.dat");
			if (loadedLevelDatFile.exists()) {
				takenGames.add(f.getName());
				String gameName = f.getName();
				//				String gameName = (new LevelDat(loadedLevelDatFile)).getGameName();
				//				takenGames.add(gameName);
				if ((new File(f, "chunkmapper")).isDirectory()) {
					gamesToDisplay.add(gameName);
				}
			}
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append(divWidth(1) + "<b>Chunkmapper Maps</b><br /><br />\n");

		numRows = gamesToDisplay.size();
		for (String gameName : gamesToDisplay) {
			String s = "<a href=\"%sResume\"><font color=\"#b0b0b0\">%s</a>"
					+ "<a href=\"%sDelete\"><font color=\"#b0b0b0\"> | Delete</a>";
			s = String.format(s, gameName, gameName, gameName);
			builder.append(s + "<br />\n");
		}
		builder.append("<br />\n");
		builder.append(divWidth(6) + "<a href=\"goto\"><font color=\"#b0b0b0\">Go To Location</a><br />");
		builder.append("<a href=\"create\"><font color=\"#b0b0b0\">Create New Chunkmap</a>");

		//		System.out.print(builder.toString());
		return builder.toString();
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

		int x = 20, y = (int) (viewport.getHeight() - 133 - viewport.getWidth() * .1) - 18 * numRows;
		return new Point(x, y);
	}

	@Override
	public String toString()
	{
		return Logging.getMessage("layers.LayerManagerLayer.Name");
	}
}
