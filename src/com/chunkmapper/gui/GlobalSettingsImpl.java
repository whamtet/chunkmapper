package com.chunkmapper.gui;

import com.chunkmapper.interfaces.GlobalSettings;

public class GlobalSettingsImpl implements GlobalSettings {

	private boolean isLive;
	private int verticalExaggeration = 1;
	
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
