package com.chunkmapper.protoc.admin;

import java.util.Iterator;

import com.chunkmapper.protoc.OSMContainer.Relation;

public class ProtocPrinter {
	public static void PrintRelation(Relation r) {
		System.out.println("*** " + r.getId());
		Iterator<String> ki = r.getKeysList().iterator(), vi = r.getValsList().iterator(), ri = r.getRolesList().iterator();
		Iterator<Long> wi = r.getWaysList().iterator();
		while (ki.hasNext()) {
			System.out.println(ki.next() + ": " + vi.next());
		}
		while (ri.hasNext()) {
			System.out.println(ri.next() + ": " + wi.next());
		}
		System.out.println("***");
	}

}
