package com.chunkmapper.binaryparser;

import java.util.ArrayList;
import java.util.Collection;

import com.chunkmapper.protoc.OSMContainer.Node;
import com.chunkmapper.protoc.OSMContainer.Relation;
import com.chunkmapper.protoc.OSMContainer.Way;

public class FileContents {
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public Collection<Way> ways = new ArrayList<Way>();
	public Collection<Relation> relations = new ArrayList<Relation>();
	public boolean isNonEmpty() {
		return nodes.size() > 0 || ways.size() > 0 || relations.size() > 0;
	}
	public int size() {
		return nodes.size() + ways.size() + relations.size();
	}
	public String toString() {
		return String.format("nodes: %s, ways: %s, relations: %s", nodes.size(), ways.size(), relations.size());
	}
}