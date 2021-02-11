package com.chunkmapper.clj;

import clojure.lang.IFn;

import static clojure.java.api.Clojure.var;

public class Clojure {

//    public static Object getVar(String ns, String v) {
//        return ((Var) var(ns, v)).get();
//    }
    public static void init() {
        load("/chunkmapper/level_dat");
    }

    private static void load(String s) {
        var("clojure.core", "load").invoke(s);
    }

    public static IFn levelDat() {
        return var("chunkmapper.level-dat", "level-dat");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(levelDat().invoke());
    }


}
