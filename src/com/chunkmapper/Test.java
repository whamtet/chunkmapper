package com.chunkmapper;

import java.io.File;
import java.util.HashMap;

import com.chunkmapper.protoc.OSMContainer;

public class Test {
	private static HashMap<Point, Point> map = new HashMap<Point, Point>();
	public static void main(String[] args) throws Exception {
		System.out.println("hi");
		OSMContainer.Relation.Builder builder = OSMContainer.Relation.newBuilder();
	}
}
