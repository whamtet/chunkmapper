package com.chunkmapper.binaryparser;

import java.util.ArrayList;
import java.util.Collection;

import com.chunkmapper.protoc.OSMContainer.Node;
import com.chunkmapper.protoc.OSMContainer.Relation;
import com.chunkmapper.protoc.OSMContainer.Way;

public class FileContents {
	
	/*
	 * OpenStreetMap data is stored as nodes (points), ways (lines) and relations (groups of lines).
	 * FileContents is an intermediate container class used when slurping up stuff from the internet or disk cache.
	 */
	
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public Collection<Way> ways = new ArrayList<Way>();
	public Collection<Relation> relations = new ArrayList<Relation>();
	public int size() {
		return nodes.size() + ways.size() + relations.size();
	}
	public String toString() {
		return String.format("nodes: %s, ways: %s, relations: %s", nodes.size(), ways.size(), relations.size());
	}
	public void append(FileContents readFile) {
		for (Node node : readFile.nodes) {
			nodes.add(node);
		}
		for (Way way : readFile.ways) {
			ways.add(way);
		}
		for (Relation relation : readFile.relations) {
			relations.add(relation);
		}
		
	}
}