package com.chunkmapper.admin;


public class GlobalSettings {

	private boolean isLive;
	private int verticalExaggeration = 1;
	public boolean gaiaMode;
	public boolean refreshNext;
	public boolean nz;
	public int generationRadius = 3;
	
	public boolean isLive() {
		// TODO Auto-generated method stub
		return isLive;
	}

	public int getVerticalExaggeration() {
		// TODO Auto-generated method stub
		return verticalExaggeration;
	}

	public void setIsLive(boolean isLive) {
		this.isLive = isLive;
		
	}

	public void setVerticalExaggeration(int verticalExaggeration) {
		this.verticalExaggeration = verticalExaggeration;
		
	}
	public String toString() {
		return super.toString() + "\nisLive: " + isLive + " verticalExaggeration: " + verticalExaggeration;
	}
	

}
