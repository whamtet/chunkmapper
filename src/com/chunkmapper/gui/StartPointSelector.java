package com.chunkmapper.gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import com.chunkmapper.Point;
import com.chunkmapper.gui.layer.GeneratingLayerImpl;
import com.chunkmapper.interfaces.MappedSquareManager;
@Deprecated
public class StartPointSelector implements MouseListener, MouseMotionListener {
	private final WorldWindow wwd;
	private final GeneratingLayerImpl generatingLayer;

	public StartPointSelector(WorldWindow wwd, GeneratingLayerImpl generatingLayer, File gameFolder) {
		this.wwd = wwd;
		this.generatingLayer = generatingLayer;
	}

	public void mouseClicked(MouseEvent arg0) {
		Position p = wwd.getCurrentPosition();
		if (p != null) {
			System.out.println(p);
			generatingLayer.notifySelected();
			
			View v = wwd.getView();
			if (v != null) {
				v.goTo(p, 512*30*10);
			}
			wwd.getInputHandler().removeMouseListener(this);
			wwd.getInputHandler().removeMouseMotionListener(this);
			((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			double lat = p.getLatitude().degrees, lon = p.getLongitude().degrees;
			generatingLayer.startThread(lat, lon);
		}

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent arg0) {
		Position p = wwd.getCurrentPosition();
		if (p != null) {
			((Component) this.wwd).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			((Component) this.wwd).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
//		
		
	}

}
