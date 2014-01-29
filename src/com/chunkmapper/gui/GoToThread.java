package com.chunkmapper.gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.chunkmapper.interfaces.GeneratingLayer;
import com.chunkmapper.parser.Nominatim;

public class GoToThread extends Thread {
	private final WorldWindow wwd;
	private final String q;
	public GoToThread(WorldWindow wwd, String q) {
		this.wwd = wwd;
		this.q = q;
	}

	public void run() {
		double[] p;
		try {
			p = Nominatim.getPoint(q);
			View v = wwd.getView();
			if (v != null) {
				v.goTo(Position.fromDegrees(p[0], p[1]), 512*30*10);
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

}
