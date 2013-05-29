package com.chunkmapper.enumeration;

public enum SurfaceType {
	WATER, EVERGREEN_NEEDLELEAF, EVERGREEN_BROADLEAF, DECIDUOUS_NEEDLELEAF, DECIDUOUS_BROADLEAF,
	MIXED_FORESTS, CLOSED_SHRUBLANDS, OPEN_SHRUBLANDS, WOODY_SAVANNAS, SAVANNAS, GRASSLANDS,
	PERMANENT_WETLANDS, CROPLANDS, URBAN_AND_BUILT_UP, NATURAL_CROPLAND, PERMANENT_SNOW, BARREN;
	
	public static SurfaceType getSurfaceCover(int i) {
		SurfaceType[] l = SurfaceType.values();
		return l[i];
	}
	public static void main(String[] args) {
		System.out.println(SurfaceType.values().length);
	}

}
