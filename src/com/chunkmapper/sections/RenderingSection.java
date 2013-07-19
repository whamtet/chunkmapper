package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class RenderingSection {
	private final Point a, b;
	private final boolean includeA;
	public final boolean isDown;
	public RenderingSection(Point p, Point a, Point b) {
		isDown = a.z > b.z;
		this.a = a;
		this.b = b;
		if (p == null) {
			includeA = true;
			return;
		}
		
		int previousSign = p.z > a.z ? -1 : 1;
		int thisSign = a.z > b.z ? -1 : 1;
		includeA = previousSign == thisSign;
		
	}
	public Integer getIntersection(int absz) {
		if (absz > a.z && absz > b.z)
			return null;
		if (absz < a.z && absz < b.z)
			return null;
		if (absz == a.z && !includeA)
			return null;
		if (absz == b.z)
			return null;
		
		return a.x + (b.x - a.x) * (absz - a.z) / (b.z - a.z);  
		
	}

}
