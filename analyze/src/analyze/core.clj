(ns analyze.core
  (:require
    [rhizome.viz :as rhizome])
  (:import
    java.io.File))

(defn take-until [f s]
  (let [[a b] (split-with (complement f) s)]
    (concat a (take 1 b))))

(defn value-map [f m]
  (zipmap (keys m) (map f (vals m))))

(defn key-filter [f m]
  (into {}
        (for [[k v] m :when (f k)]
          [k v])))

(defn main-declaration? [s]
  (.startsWith s "public"))

(defn update-raw [m line]
  (let [[a & rest] (map #(.trim %) (.split line " "))]
    (update m a conj rest)))

(defn decommarize [[s]]
  (.replace s ";" ""))

(defn finalize [{:strs [public package import]}]
  [(str
     (-> package first decommarize)
     "."
     (-> public first second))
   (map decommarize import)])

(defn parse [f]
  (as-> (slurp f) $
        (.split $ "\n")
        (take-until main-declaration? $)
        (reduce update-raw {} $)
        (finalize $)))

(defn src? [f]
  (.endsWith (.getName f) ".java"))

(def src (->> "../src"
              File.
              file-seq
              (filter src?)))

(defn to-ignore? [k]
  (or
    (contains? #{"com.chunkmapper.Point" "com.chunkmapper.admin.MyLogger"} k)
    (.startsWith k "com.chunkmapper.protoc")
    (.startsWith k "com.chunkmapper.gui")
    (.startsWith k "com.chunkmapper.nbt")))

(def raw-deps
  (as-> src $
       (map parse $)
       (key-filter (complement to-ignore?) $)))

(def final-deps
  (value-map (fn [deps] (filter #(contains? raw-deps %) deps)) raw-deps))

(defn descriptor [n]
  {:label (-> n (.split "\\.") last)})

(spit "inspect.svg"
      (rhizome/graph->svg (keys final-deps) final-deps :node->descriptor descriptor))
