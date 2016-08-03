(ns bio.file-io
  (:require [clojure.java.io :as io :refer [reader file]])
  (:require [clojure.string :as s :refer [split trim]]))

(def data-columns [:last_name :first_name :gender :favorite_color :dob])
(def record-delimiter (re-pattern "\\s[|]\\s|[,]\\s|\\s"))

(defn assign-keys 
  "Splits string on delimiter, zips to map and appends to given result vector"
  [result line]
  (->> (s/split (trim line) record-delimiter) 
    (zipmap data-columns)
    (conj result)))

(defn parse-file [result file]
  "Lazy-read each line from file, reduces on key assignment method & append results to given vector" 
  (with-open [r (io/reader file)]
   (reduce conj result (reduce assign-keys [] (line-seq r)))))

(defn extract-records 
  "Turns path (can be directory or file) into seq of files and reduces on extraction method"
  [path]
    (->> (filter #(.isFile %) (file-seq (io/file path))) 
      (reduce parse-file [])))
