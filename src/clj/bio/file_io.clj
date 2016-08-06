(ns bio.file-io
  (:require [clojure.java.io :as io :refer [reader file]]
	    [clojure.string :as s :refer [trim split join]]
	    [clj-time.format :as tformat :refer [formatter parse unparse]]))

(def data-columns [:last_name :first_name :gender :favorite_color :dob])
(def record-delimiters (re-pattern "\\s[|]\\s|[,]\\s|\\s"))
(def date-formatter (formatter "MM/dd/YYYY"))
(def date-format  "MM/dd/YYYY")
(def default-delimiter ", ")

(defn parse-date [date]
  (tformat/parse (tformat/formatter date-format) date))

(defn unparse-date [date]
  (tformat/unparse (tformat/formatter date-format) date))

(defn format-date [f record]
  (update record :dob #(f %)))
  
(defn assign-keys 
  "Splits string on delimiter, zips to map and appends to given result vector"
  [result line]
  (->> (s/split (s/trim line) record-delimiters) 
    (zipmap data-columns)
    (format-date parse-date)
    (conj result)))

(defn cli-format 
  "Unparses time format & extracts values from records to produce delimited string for printing"
  [record] 
  (->> record
    (format-date unparse-date)
    (vals)
    (s/join default-delimiter)))

(defn parse-file
  "Lazy-read each line from file, reduces on key assignment method & append results to given vector" 
  [result file]
  (with-open [r (io/reader file)]
   (reduce conj result (reduce assign-keys [] (line-seq r)))))

(defn configurable-sort   
  "Switches ascending or descending order based on :order key in sort-config map,
   and compares given args (maps) based on symbolized value in key to sort on (:sym-key)"
  [sort-config comp1 comp2]
  (let [{:keys [sym-key order]} sort-config]
    (if (= order :desc)
      (compare (sym-key comp2) (sym-key comp1))
      (compare (sym-key comp1) (sym-key comp2)))))

(defn kv-sort 
  "Recursively calls configurable-sort on sort-configs until result != 0 or the configs have been exhausted"
  ([sort-configs comp1 comp2] (kv-sort sort-configs comp1 comp2 0))
  ([sort-configs comp1 comp2 result] 
    (if (or (empty? sort-configs) (not (zero? result))) 
      result
      (recur (rest sort-configs) comp1 comp2 (configurable-sort (first sort-configs) comp1 comp2))))) 
	
(defn multisort 
  "Sort config requires :order and :sym-key and should be seq-able i.e. [{:sym-key :sort-key :order :desc}]"
  ([coll sort-configs] (sort #(kv-sort sort-configs %1 %2) coll)))

(defn print-records [coll]
  (dorun (map #(prn (cli-format %)) coll)))

(defn extract-records 
  "Turns any number of paths (directories or files) into seq of files and reduces on extraction method"
  [& paths]
    (->>  
      (distinct (reduce into (map #(file-seq (io/file %)) paths)))
      (filter #(.isFile %)) 
      (reduce parse-file [])))
