/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package com.chunkmapper.gui.layer;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.util.Logging;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.swing.JFrame;

import com.chunkmapper.GameMetaInfo;
import com.chunkmapper.ManagingThread;
import com.chunkmapper.Point;
import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.gui.MappedSquareManagerImpl;
import com.chunkmapper.gui.PlayerIconManagerImpl;
import com.chunkmapper.gui.StartPointSelector;
import com.chunkmapper.gui.dialog.NewMapDialog.NewGameInfo;
import com.chunkmapper.gui.dialog.TeleportDialog;
import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.writer.LevelDat;


public class GeneratingLayerImpl extends RenderableLayer implements SelectListener, GeneratingLayer
{
	protected WorldWindow wwd;
	protected boolean update = true;

	private ScreenAnnotation parentAnnotation;
	protected Dimension size;
	private Color color = Color.decode("#b0b0b0");
	private Color highlightColor = Color.decode("#ffffff");
	private double minOpacity = .6;
	private double maxOpacity = 1;
	private Font font = new Font("SansSerif", Font.PLAIN, 14);

	private final File gameFolder;
	private final NewGameInfo gameInfo;
	private final MainLayer mainLayer;
	private StartPointSelector selector;
//	private boolean awaitingSelectPoint;
	private ManagingThread managingThread;
	private MappedSquareManagerImpl mappedSquareManager;
	private PlayerIconManagerImpl playerIconManager;
	private final JFrame appFrame;
	private final GlobalSettings globalSettings;
	private boolean isCancelling;
	private final LevelDat levelDat;


	public GeneratingLayerImpl(WorldWindow wwd, JFrame appFrame, File savesDir, NewGameInfo info, MainLayer mainLayer, GlobalSettings globalSettings) throws IOException
	{
		this.globalSettings = globalSettings;
		this.appFrame = appFrame;
		gameFolder = new File(savesDir, info.gameName);
		boolean awaitingSelectPoint = !gameFolder.exists();

		this.mainLayer = mainLayer;
		this.gameInfo = info;

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

		levelDat = new LevelDat(new File(gameFolder, "level.dat"), null);
		// Listen to world window for select event
		this.wwd.addSelectListener(this);
		if (awaitingSelectPoint) {
			startCenteredThread();
			//skip selection of point, just choose middle point in view.
		} else {
			//need to read in player position
			try {
					Point relativePlayerPoint = levelDat.getPlayerPosition();
					GameMetaInfo gameMetaInfo = new GameMetaInfo(gameFolder, 0, 0, 0, false);
					double lat = - (relativePlayerPoint.z + gameMetaInfo.rootPoint.z * 512) / 3600.;
					double lon = (relativePlayerPoint.x + gameMetaInfo.rootPoint.x * 512) / 3600.;
					wwd.getSceneController().setVerticalExaggeration(gameMetaInfo.verticalExaggeration);
					startThread(lat, lon);
			} catch (NumberFormatException e) {
				//also make sure that the file is deleted.
				File chunkmapperDir = new File(gameFolder, "chunkmapper");
				(new File(chunkmapperDir, GameMetaInfo.STORE_NAME)).delete();
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				//same as if awaitingSelectPoint
				startCenteredThread();
				
			}
		}
		
	}
	//instead of choosing start point, just default to center position
	private void startCenteredThread() {
		View v = wwd.getView();
		final Position p = v.getCurrentEyePosition();
		if (p != null) {
			System.out.println(p);
			
			if (v != null) {
				v.goTo(p, 512*30*10);
			}
			((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			double lat = p.getLatitude().degrees, lon = p.getLongitude().degrees;
			startThread(lat, lon);
		}
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.gui.GeneratingLayer#zoomTo()
	 */
	public void zoomTo(double lat, double lon) {
		View v = wwd.getView();
		if (v != null) {
			v.goTo(Position.fromDegrees(lat, lon), 512*30*10);
		}
	}
	public void startThread(double lat, double lon) {
		zoomTo(lat, lon);
		mappedSquareManager = new MappedSquareManagerImpl(wwd);
		playerIconManager = new PlayerIconManagerImpl(lat, lon, wwd);
		managingThread = new ManagingThread(lat, lon, gameFolder, mappedSquareManager, playerIconManager,
				globalSettings, this, gameInfo);
		managingThread.start();
	}
	public void cancel() {
		cancel(false);
	}
	public void cancel(final boolean selfCalled) {

		isCancelling = true;
		update();
		Thread t = new Thread() {
			public void run() {
				if (selector != null) {
					wwd.getInputHandler().removeMouseListener(selector);
					wwd.getInputHandler().removeMouseMotionListener(selector);
					((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				if (managingThread != null) {
					ManagingThread.blockingShutDown(managingThread, selfCalled);
//					managingThread.interrupt();
					managingThread = null;
				}
				if (mappedSquareManager != null) {
					mappedSquareManager.remove();
				}
				if (playerIconManager != null) {
					playerIconManager.remove();
				}
				wwd.getModel().getLayers().remove(GeneratingLayerImpl.this);
				wwd.getModel().getLayers().add(mainLayer);
				wwd.getSceneController().setVerticalExaggeration(1);
			}
		};
		t.start();
	}
//	private void teleport() {
//		(new TeleportDialog(appFrame, gameName, levelDat, gameMetaInfo.rootPoint)).setVisible(true);
//	}

	public void selected(SelectEvent event) {

		if (event.hasObjects() && event.getTopObject() == this.parentAnnotation)
		{
			boolean update = false;

			PickedObject po = event.getTopPickedObject();
			if (po != null) {
				String s = (String) po.getValue(AVKey.URL);
				if (s != null) {
					((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
//						if (s.equals("tp")) {
//							teleport();
//						}
						if (s.equals("cancel")) {
							cancel();
						}
						if (s.equals("zoom")) {
							View v = wwd.getView();
							if (v != null) {
								LatLon ll = playerIconManager.getLatLon();
								v.goTo(Position.fromDegrees(ll.latitude.degrees, ll.longitude.degrees), 512*30*10);
							}
						}
						if (s.equals("gm") || s.equals("osm") || s.equals("browser")) {
							
							LatLon ll = playerIconManager.getLatLon();
							if (s.equals("browser")) {
								int regionx = (int) Math.floor(ll.longitude.degrees * 3600 / 512);
								int regionz = (int) Math.floor(-ll.latitude.degrees * 3600 / 512);
								//String t = String.format("http://www.chunkmapper.com/leaflet2.html?regionx=%s&regionz=%s", regionx, regionz);
								String t = String.format("http://localhost:5000/leaflet2.html?regionx=%s&regionz=%s", regionx, regionz);
								try {
									openWebpage(new URI(t));
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
							} else if (s.equals("gm")) {
//								
								double lat = ll.latitude.degrees, lon = ll.longitude.degrees;
								String latStr = lat < 0 ? "S" : "N";
								String lonStr = lon < 0 ? "W" : "E";
								String s2 = URLEncoder.encode(String.format("/maps/place/%s%s+%s%s", decimalToString(lat), latStr, decimalToString(lon), lonStr));
								try {
									openWebpage(new URI("https://www.google.com" + s2));
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								try {
									openWebpage(new URI(String.format("http://www.openstreetmap.org/index.html?lat=%s&lon=%s&zoom=15"
											, ll.latitude.degrees, ll.longitude.degrees)));
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				} else {
					((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			if (event.getEventAction().equals(SelectEvent.ROLLOVER)
					|| event.getEventAction().equals(SelectEvent.LEFT_CLICK))
			{
				// Highlight annotation
				if (!this.parentAnnotation.getAttributes().isHighlighted())
				{
					this.parentAnnotation.getAttributes().setHighlighted(true);
					update = true;
				}
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
	//
	private static String decimalToString(double d) {
		if (d < 0)
			d = -d;
		int deg = (int) d;
		d = 60 * (d - deg);
		int min = (int) d;
		d = 60 * (d - min);
		double sec = d;
		StringBuilder sb = new StringBuilder();
		sb.append(deg);
		sb.append("�");
		sb.append(min);
		sb.append("'");
		sb.append(String.format("%.1f\"", sec));
		return sb.toString();
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
	public void notifySelected() {
//		this.awaitingSelectPoint = false;
		this.update();
	}
	protected String makeAnnotationText(LayerList layers)
	{
		if (isCancelling) {
			return "Cancelling...";
		}
//		if (awaitingSelectPoint) {
//			return divWidth(7) + "Click Globe <br />\n"
//					+ "To Select Start Point<br /><br />\n"
//					+ divWidth(14) + "<font color=\"#b0b0b0\">*** <br />\n"
//					+ divWidth(11) + "<a href=\"cancel\"><font color=\"#b0b0b0\">Cancel</a>";
//		}
		return String.format("<b>Generating %s...</b><br /><br />\n", gameInfo.gameName)
				+ "Blue Boxes Show Map Extent<br /><br />\n"
				+ divWidth(5) + "<a href=\"zoom\"><font color=\"#b0b0b0\">Center Current Position</a><br />"
				+ divWidth(11) + "<a href=\"browser\"><font color=\"#b0b0b0\">Show in Browser</a><br />"
				+ divWidth(5) + "<a href=\"osm\"><font color=\"#b0b0b0\">Show in OpenStreetMap</a><br />"
//				+ divWidth(12) + "<a href=\"tp\"><font color=\"#b0b0b0\">Teleport Player</a><br />"
				+ divWidth(21) + "<font color=\"#b0b0b0\">***<br />"
				+ divWidth(18) + "<a href=\"cancel\"><font color=\"#b0b0b0\">Cancel</a>";
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
		int x;
		int y;
		if (isCancelling) {
			y = (int) (viewport.getHeight() - 80 - viewport.getWidth() * .1);
			x = (int) (20 + viewport.getWidth() * 0.04);
//		} else if (awaitingSelectPoint) {
//			y = (int) (viewport.getHeight() - 150 - viewport.getWidth() * .1);
//			x = 20;
		} else {
			y =	(int) (viewport.getHeight() - 190 - viewport.getWidth() * .1) - 15;
			x = 20;
		}
		return new Point(x, y);
	}

	public String toString()
	{
		return Logging.getMessage("layers.LayerManagerLayer.Name");
	}
	//this is abstract and must be overwridden
	public void zoomTo() {
		//this is abstract and must be overwridden
		
	}
}
