package com.chunkmapper.gui.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.chunkmapper.Point;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.gui.PlayerIconManagerImpl;
import com.chunkmapper.interfaces.MappedSquareManager;
import com.chunkmapper.interfaces.PlayerIconManager;
import com.chunkmapper.math.Matthewmatics;

public class MapPanel extends JPanel implements PlayerIconManager, MappedSquareManager {
	public MapPanel() {
	}

	private HashSet<Point> regionsMade = new HashSet<Point>();
	private Point playerPosition;
	private BufferedImage icon, compass;
	private int regionx1 = Integer.MAX_VALUE, regionz1 = Integer.MAX_VALUE;
	private int regionx2 = Integer.MIN_VALUE, regionz2 = Integer.MIN_VALUE;
	
	{
		try {
			URL iconSource = PlayerIconManagerImpl.class.getResource("/steve2.jpeg");
			icon = ImageIO.read(iconSource);

			URL compassSource = MapPanel.class.getResource("/compass_rose.png");
			compass = ImageIO.read(compassSource);
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLUE);
		int w = this.getWidth() / (regionx2 - regionx1 + 1);
		int h = this.getHeight() / (regionz2 - regionz1 + 1);
		for (Point p : regionsMade) {
			int x = (p.x - regionx1) * w, y = (p.z - regionz1) * h;
			g.fillRect(x, y, w, h);
		}
		if (playerPosition != null) {
			int x = (playerPosition.x - regionx1*512) * this.getWidth() / 512 / (regionx2 - regionx1 + 1);
			int y = (playerPosition.z - regionz1 * 512) * this.getHeight() / 512 / (regionz2 - regionz1 + 1);
			g.drawImage(icon, x - icon.getWidth() / 2, y - icon.getHeight() / 2, null);
		}
		g.drawImage(compass, 0, this.getHeight() - compass.getHeight(), null);
	}
	@Override
	public void setLocation(double lat, double lon) {
		playerPosition = new Point(lon * 3600, - lat * 3600);
		int regionx = Matthewmatics.div(playerPosition.x, 512);
		int regionz = Matthewmatics.div(playerPosition.z, 512);
		
		if (regionx < regionx1) regionx1 = regionx;
		if (regionx > regionx2) regionx2 = regionx;
		if (regionz < regionz1) regionz1 = regionz;
		if (regionz > regionz2) regionz2 = regionz;
		repaint();
	}
	@Override
	public void addFinishedPoint(Point p) {
		regionsMade.add(p);
		if (p.x < regionx1) regionx1 = p.x;
		if (p.z < regionz1) regionz1 = p.z;
		if (p.x > regionx2) regionx2 = p.x;
		if (p.z > regionz2) regionz2 = p.z;
		repaint();
	}
	@Override
	public void addUnfinishedPoint(Point p) {
		// TODO Auto-generated method stub
		
	}


}