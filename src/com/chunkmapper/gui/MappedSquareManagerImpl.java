package com.chunkmapper.gui;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Box;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.interfaces.MappedSquareManager;

public class MappedSquareManagerImpl implements MappedSquareManager {
	//	private final WorldWindow wwd;
	private final HashSet<Point> addedPoints = new HashSet<Point>();
	private final HashMap<Point, Box> unfinishedPoints = new HashMap<Point, Box>();
	private final RenderableLayer layer = new RenderableLayer();
	public final static double MAX_HEIGHT = 256 * Utila.Y_SCALE;
	private final WorldWindow wwd;
	
	public static Path getPath(Position a, Position b) {
		Path path = new Path(a, b);
		path.setPathType(AVKey.LINEAR);
		return path;
	}

	private static ShapeAttributes makeAttrs(Material m) {
		ShapeAttributes attrs = new BasicShapeAttributes();
		attrs.setInteriorMaterial(m);
		attrs.setInteriorOpacity(0.1);
		attrs.setEnableLighting(true);
		attrs.setOutlineMaterial(Material.RED);
		attrs.setOutlineWidth(2d);
		attrs.setDrawInterior(true);
		attrs.setDrawOutline(false);
		return attrs;
	}
	private static Double lat, lon;
	private static Box makeBox(Point p, Material m) {
		lat = -(p.z + .5) * 512 / 3600; 
		lon = (p.x + 0.5)*512/3600;
		
		double altRad = 256 * Utila.Y_SCALE;
		int latRad = 256 * Utila.Y_SCALE;
		double lonRad = latRad * Math.cos(lat * Math.PI / 180);
		Box box3 = new Box(Position.fromDegrees(lat, lon, 0), latRad, altRad, lonRad);
		
		box3.setAltitudeMode(WorldWind.ABSOLUTE);
		box3.setAttributes(makeAttrs(m));
		box3.setVisible(true);
		
		return box3;
	}
	public MappedSquareManagerImpl(WorldWindow wwd) {
		this.wwd = wwd;
		ApplicationTemplate.insertBeforeCompass(wwd, layer);
	}

	public void addFinishedPoint(Point p) {
		if (!addedPoints.contains(p)) {
			Box oldBox = unfinishedPoints.get(p);
			if (oldBox != null)
				layer.removeRenderable(oldBox);
			addedPoints.add(p);
			layer.addRenderable(makeBox(p, Material.BLUE));
			wwd.redraw();
		}
	}
	
	public void remove() {
		wwd.getModel().getLayers().remove(layer);
	}

	@Override
	public void addUnfinishedPoint(Point p) {
		//Potential race condition here, probably not that important.
		if (!unfinishedPoints.containsKey(p)) {
			Box b = makeBox(p, Material.RED);
			unfinishedPoints.put(p, b);
			layer.addRenderable(b);
			wwd.redraw();
		}
		
	}

}
