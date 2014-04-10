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

public class HorizontalPath extends Path implements Movable {
	private double lat, lon1, lon2;
	
	public HorizontalPath(double lat, double lon1, double lon2) {
		
		this.lat = lat;
		this.lon1 = lon1;
		this.lon2 = lon2;
		
        ShapeAttributes attrs = new BasicShapeAttributes();
        attrs.setOutlineMaterial(Material.ORANGE);
        attrs.setInteriorMaterial(Material.ORANGE);
        attrs.setOutlineWidth(2);

        setAttributes(attrs);
        setPathType(AVKey.LINEAR);
        setExtrude(true);
        
        super.setPositions(getEndPoints(lat, lon1, lon2));
	}
	private static List<Position> getEndPoints(double lat, double lon1, double lon2) {
		double alt = 256 * Utila.Y_SCALE;
        double incr = 512./ 3600;
		List<Position> endPoints = new ArrayList<Position>();
		for (double lon = lon1; lon < lon2; lon += incr) {
			endPoints.add(Position.fromDegrees(lat, lon, alt));
		}
		endPoints.add(Position.fromDegrees(lat, lon2, alt));
		return endPoints;
	}
	@Override
	public void moveTo(Position position) {
		lat = position.getLatitude().degrees;
		setPositions(getEndPoints(lat, lon1, lon2));
	}
	public void setLon1(double lon1) {
		this.lon1 = lon1;
		setPositions(getEndPoints(lat, lon1, lon2));
	}
	public void setLon2(double lon2) {
		this.lon2 = lon2;
		setPositions(getEndPoints(lat, lon2, lon2));
	}

}
