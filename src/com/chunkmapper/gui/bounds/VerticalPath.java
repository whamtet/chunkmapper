package com.chunkmapper.gui.bounds;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;

import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.admin.Utila;

public class VerticalPath extends Path implements Movable {
	private double lon, lat1, lat2;
	
	public VerticalPath(double lon, double lat1, double lat2) {
		
		this.lon = lon;
		this.lat1 = lat1;
		this.lat2 = lat2;
		
        ShapeAttributes attrs = new BasicShapeAttributes();
        attrs.setOutlineMaterial(Material.ORANGE);
        attrs.setInteriorMaterial(Material.ORANGE);
        attrs.setOutlineWidth(2);

        setAttributes(attrs);
        setPathType(AVKey.LINEAR);
        setExtrude(true);
        
        super.setPositions(getEndPoints(lon, lat1, lat2));
	}
	private static List<Position> getEndPoints(double lon, double lat1, double lat2) {
		double alt = 256 * Utila.Y_SCALE;
        double incr = 512./ 3600;
		List<Position> endPoints = new ArrayList<Position>();
		for (double lat = lat1; lat < lat2; lat += incr) {
			endPoints.add(Position.fromDegrees(lat, lon, alt));
		}
		endPoints.add(Position.fromDegrees(lat2, lon, alt));
		return endPoints;
	}
	@Override
	public void moveTo(Position position) {
		lon = position.getLongitude().degrees;
		setPositions(getEndPoints(lon, lat1, lat2));
	}
	public void setLat1(double lat1) {
		this.lat1 = lat1;
		setPositions(getEndPoints(lon, lat1, lat2));
	}
	public void setLat2(double lat2) {
		this.lat2 = lat2;
		setPositions(getEndPoints(lon, lat1, lat2));
	}

}
