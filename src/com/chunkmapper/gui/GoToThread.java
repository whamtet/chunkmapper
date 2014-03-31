package com.chunkmapper.gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.gui.ApplicationTemplate.AppFrame;
import com.chunkmapper.parser.Nominatim;

public class GoToThread extends Thread {
	private final WorldWindow wwd;
	private final String q;
	private final JFrame appFrame;
	public GoToThread(JFrame appFrame2, WorldWindow wwd, String q) {
		this.wwd = wwd;
		this.q = q;
		this.appFrame = appFrame2;
	}

	public void run() {
		double[] p;
		try {
			p = Nominatim.getPoint(q);
			if (p == null) {
				JOptionPane.showMessageDialog(appFrame, "Location Not Found.", "", JOptionPane.ERROR_MESSAGE);
			} else {
				View v = wwd.getView();
				if (v != null) {
					v.goTo(Position.fromDegrees(p[0], p[1]), 512*30*10);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void placeNotFound() {
		JOptionPane.showMessageDialog(null, "Location not Found");
	}
	public static void main(String[] args) throws Exception {
		placeNotFound();
	}

}
