package com.chunkmapper.gui;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Box;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;

import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.interfaces.MappedSquareManager;

public class MappedSquareManagerImpl implements MappedSquareManager {
	//	private final WorldWindow wwd;
	private final HashSet<Point> addedPoints = new HashSet<Point>();
	private final RenderableLayer layer = new RenderableLayer();
	public final static double MAX_HEIGHT = 256 * Utila.Y_SCALE;
	private ShapeAttributes attrs;
	private final WorldWindow wwd;

	public MappedSquareManagerImpl(WorldWindow wwd) {
		this.wwd = wwd;
		ApplicationTemplate.insertBeforeCompass(wwd, layer);
		attrs = new BasicShapeAttributes();
		attrs.setInteriorMaterial(Material.BLUE);
		attrs.setInteriorOpacity(0.3);
		attrs.setEnableLighting(true);
		attrs.setOutlineMaterial(Material.RED);
		attrs.setOutlineWidth(2d);
		attrs.setDrawInterior(true);
		attrs.setDrawOutline(false);
	}

	public void addPoint(Point p) {
		if (!addedPoints.contains(p)) {			
			addedPoints.add(p);
			double lat = -(p.z + .5) * 512 / 3600, lon = (p.x + 0.5)*512/3600;
			double altRad = 256 * Utila.Y_SCALE;
			int latRad = 256 * Utila.Y_SCALE;
			double lonRad = latRad * Math.cos(lat * Math.PI / 180);
			Box box3 = new Box(Position.fromDegrees(lat, lon, 0), latRad, altRad, lonRad);
			box3.setAltitudeMode(WorldWind.ABSOLUTE);
			box3.setAttributes(attrs);
			box3.setVisible(true);
			layer.addRenderable(box3);
			wwd.redraw();
		}
	}
	public void addPoint(int regionx, int regionz) {
		addPoint(new Point(regionx, regionz));
	}
//	public void addDone() throws IOException {
//		double lon1 = 165.88, lat1 = -47.5, lon2 = 178.7, lat2 = -34.08;
//		int regionx1 = (int) Math.floor(lon1 * 3600 / 512);
//		int regionz1 = (int) Math.floor(-lat1 * 3600 / 512);
//		int regionx2 = (int) Math.floor(lon2 * 3600 / 512);
//		int regionz2 = (int) Math.floor(-lat2 * 3600 / 512);
//		for (int x = regionx1; x <= regionx2; x++) {
//			for (int z = regionz2; z < regionz1; z++) {
//				ResourceInfo info = new HeightsResourceInfo(x, z);
//				if (FileValidator.checkValid(info.file)) {
//					addPoint(x, z);
//				}
//			}
//		}
//	}
	public void remove() {
		wwd.getModel().getLayers().remove(layer);
	}

}
