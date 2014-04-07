package com.chunkmapper.gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.parser.Nominatim;

public class GoToSwingWorker extends SwingWorker<double[], Void> {
	private final WorldWindow wwd;
	private final String q;
	private final JFrame appFrame;
	public GoToSwingWorker(JFrame appFrame2, WorldWindow wwd, String q) {
		this.wwd = wwd;
		this.q = q;
		this.appFrame = appFrame2;
	}

	@Override
	public double[] doInBackground() {
		return Nominatim.getPointSafe(q);
	}
	@Override
	public void done() {
		double[] latlon = null;
		try {
			latlon = this.get();
		} catch (InterruptedException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
			return;
		} catch (ExecutionException e) {
			MyLogger.LOGGER.warning(MyLogger.printException(e));
		}
		if (latlon == null) {
			JOptionPane.showMessageDialog(appFrame, "Location Not Found.", "", JOptionPane.ERROR_MESSAGE);
		} else {
			View v = wwd.getView();
			if (v != null) {
				v.goTo(Position.fromDegrees(latlon[0], latlon[1]), 512*30*10);
			}
		}
	}
}
