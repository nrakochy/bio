(ns bio.file-io
  (:require [clojure.java.io :as io :refer [reader file]])
  (:require [clojure.string :as s :refer [split trim]]))

(def data-columns [:last_name :first_name :gender :favorite_color :dob])
(def record-delimiter (re-pattern "\\s[|]\\s|[,]\\s|\\s"))

(defn assign-keys [result line]
  (->> (s/split (trim line) record-delimiter) 
    (zipmap data-columns)
    (conj result)))

(defn reduce-keys [coll]
  (reduce assign-keys [] (filter #(not (empty? %)) coll)))

(defn extract-records [file]
  (with-open [r (io/reader file)]
   (reduce-keys (line-seq r))))
