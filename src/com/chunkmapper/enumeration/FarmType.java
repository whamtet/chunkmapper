package com.chunkmapper.enumeration;

public enum FarmType {
	Cows, Chicken, Sheep, Wheat, Potatoes, Carrots;
	public static final int COW_DENSITY = 3, SHEEP_DENSITY = 4, CHICKEN_DENSITY = 5;
	public static FarmType[] getFarmTypes() {
		return new FarmType[] {Cows, Chicken, Sheep, Wheat, Wheat, Wheat, Potatoes, Potatoes, Potatoes, 
				Carrots, Carrots, Carrots};
		}

}
