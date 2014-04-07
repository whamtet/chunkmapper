package com.chunkmapper.gui;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.IconLayer;
import gov.nasa.worldwind.render.UserFacingIcon;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.net.URL;

import com.chunkmapper.interfaces.PlayerIconManager;

public class PlayerIconManagerImpl implements PlayerIconManager {
	private final UserFacingIcon icon;
	private final IconLayer layer;
	private final WorldWindow wwd;
	private double lat, lon;
	public PlayerIconManagerImpl(double lat, double lon, WorldWindow wwd) {
		this.lat = lat; this.lon = lon;
		this.wwd = wwd;
		layer = new IconLayer();
		URL iconSource = PlayerIconManagerImpl.class.getResource("/steve2.jpeg");
        icon = new UserFacingIcon(iconSource, Position.fromDegrees(lat, lon));
        layer.addIcon(icon);
        ApplicationTemplate.insertAfterPlacenames(wwd, layer);
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.gui.PlayerIconManagerInterface#setLocation(double, double)
	 */
	public void setLocation(double lat, double lon) {
		this.lat = lat; this.lon = lon;
		icon.setPosition(Position.fromDegrees(lat, lon));
	}
	public void remove() {
		wwd.getModel().getLayers().remove(layer);
	}
	public LatLon getLatLon() {
		return LatLon.fromDegrees(lat, lon);
	}

}
