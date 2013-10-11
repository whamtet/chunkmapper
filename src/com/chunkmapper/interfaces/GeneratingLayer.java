package com.chunkmapper.interfaces;

public interface GeneratingLayer {

	public void zoomTo();
	public void zoomTo(double lat, double lon);

	public void cancel();

}