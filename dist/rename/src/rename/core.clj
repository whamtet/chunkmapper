(ns rename.core)

(import java.io.File)

(def jars (filter #(and (.endsWith % ".jar") (not (.contains % "natives")))
                  (map #(.getPath %)
                       (file-seq (File. "../../target/dependency")))))

(def new-names (map #(format "../../target/dependency/f%02d.jar" %) (range)))

(defn self-map [& args] (zipmap args args))

(def renamed (merge
               (zipmap jars new-names)
               (self-map
                 "../../target/dependency/gluegen-rt.jar"
                 "../../target/dependency/jogl-all.jar")))

(doseq [[old new] renamed]
    (.renameTo (File. old) (File. new)))

(def cp (apply str (interpose ":" (map #(.replace % "../../target/dependency" ".") (vals renamed)))))
(println cp)
