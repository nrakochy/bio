(ns bio.file-io
  (:require [clojure.java.io :as io :refer [reader file]]
	    [bio.data-processing :as dp :refer [assign-keys cli-format]]))

(defn parse-file
  "Lazy-read each line from file, reduces on key assignment method & append results to given vector" 
  [result file]
  (with-open [r (io/reader file)]
   (reduce conj result (reduce dp/assign-keys [] (line-seq r)))))

(defn print-records 
  "Formats & prints seq to newline"
  [coll]
  (dorun (map #(prn (dp/cli-format %)) coll)))

(defn extract-records 
  "Turns any number of paths (directories or files) into seq of files and reduces on extraction method"
  [paths]
    (->>  
      (distinct (reduce into (map #(file-seq (io/file %)) paths)))
      (filter #(.isFile %)) 
      (reduce parse-file [])))
