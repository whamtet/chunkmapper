package com.chunkmapper;

public interface GeneratingLayer {

	public void zoomTo();
	public void zoomTo(double lat, double lon);

	public void cancel();

}