package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class Section {
	private Point a, b;
	private boolean includeA;
	public Section(Point p, Point a, Point b) {
		
		int previousSign = p.z > a.z ? -1 : 1;
		int thisSign = a.z > b.z ? -1 : 1;
		includeA = previousSign == thisSign;
		
		this.a = a;
		this.b = b;
		
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
