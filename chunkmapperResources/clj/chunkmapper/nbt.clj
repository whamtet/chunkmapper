(ns chunkmapper.nbt
  (:import
    [com.chunkmapper.nbt
     ByteArrayTag
     ByteTag
     CompoundTag
     DoubleTag
     EndTag
     FloatTag
     IntArrayTag
     IntTag
     ListTag
     LongArrayTag
     LongTag
     ShortTag
     StringTag]))

(defn ->nbt
  ([m] (->nbt "" m))
  ([s m]
   (cond
     (seq? m)
     (let [t (ListTag. s)]
       (doseq [i m]
         (.add t (->nbt "" i)))
       t)
     (map? m)
     (let [t (CompoundTag. s)]
       (doseq [[k v] m]
         (.put t (name k) (->nbt (name k) v)))
       t)
     (string? m)
     (StringTag. s m)
     :else ;; must be a vector
     (let [[type data] m]
       (case type
         :byte (ByteTag. s data)
         :short (ShortTag. s data)
         :int (IntTag. s data)
         :long (LongTag. s data)
         :float (FloatTag. s data)
         :double (DoubleTag. s data)
         :byte-array (ByteArrayTag. s (into-array Byte/TYPE data))
         :int-array (IntArrayTag. s (into-array Integer/TYPE data))
         :long-array (LongArrayTag. s (into-array Long/TYPE data)))))))

(defmacro class-case [test & pairs]
  (let [x (gensym)]
    `(let [~x ~test]
       (cond
         ~@(for [[class result] (partition 2 pairs)
                 data [`(instance? ~class ~x) result]]
             data)
         ~@(when (odd? (count pairs))
             [:else (last pairs)])))))

(defn ->edn [tag]
  (class-case
    tag
    ListTag (map ->edn (.getData tag))
    CompoundTag (into {} (for [[k v] (.getData tag)]
                           [(keyword k) (->edn v)]))
    StringTag (str tag)
    ByteArrayTag [:byte-array (seq (.getData tag))]
    IntArrayTag [:int-array (seq (.getData tag))]
    LongArrayTag [:long-array (seq (.getData tag))]
    [({ByteTag :byte
       ShortTag :short
       IntTag :int
       LongTag :long
       FloatTag :float
       DoubleTag :double} (class tag)) (.getData tag)]))


;; (def edn
;;   (into
;;     {:seq (list "seq1")
;;      :string "string"}
;;     (concat
;;       (for [k [:byte :short :int :long]]
;;         [k [k 1]])
;;       (for [k [:float :double]]
;;         [k [k 1.0]])
;;       (for [k [:byte-array :int-array :long-array]]
;;         [k [k [1]]]))))
