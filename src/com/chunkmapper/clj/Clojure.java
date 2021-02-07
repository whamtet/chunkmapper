package com.chunkmapper.clj;

import clojure.lang.IFn;
import clojure.lang.Var;
import com.chunkmapper.nbt.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static clojure.java.api.Clojure.var;

public class Clojure {

    public static Object getVar(String ns, String v) {
        return ((Var) var(ns, v)).get();
    }

    public static void main(String[] args) throws Exception {
        var("clojure.core", "load").invoke("/clj/chunkmapper/nbt");
    }
}
