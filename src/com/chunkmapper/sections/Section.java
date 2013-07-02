package com.chunkmapper.sections;

import com.chunkmapper.Point;

public class Section {
	private Point a, b;
	private boolean aEnd, bEnd;
	public final boolean isHorizontal;
	
	public Section(Point p, Point a, Point b, Point n) {
		isHorizontal = a.z == b.z;
		if (isHorizontal)
			return;
		if (a.z < b.z) {
			//need to swap
			Point c = b;
			b = a;
			a = c;
		}
		this.a = a;
		this.b = b;
		
		aEnd = a.z < p.z && a.z < b.z || a.z > p.z && a.z > b.z;
		bEnd = b.z < a.z && b.z < n.z || b.z > a.z && b.z > n.z;
		
	}

	public Integer getIntersection(int absz) {
		if (absz > a.z || absz < b.z)
			return null;
		if (absz == a.z && aEnd)
			return null;
		if (absz == b.z && bEnd)
			return null;
		
		return b.x + (a.x - b.x) * (absz - b.z) / (a.z - b.z); 
	}
	

}
