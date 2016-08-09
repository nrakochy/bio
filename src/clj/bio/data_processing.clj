(ns bio.data-processing
  (:require [clojure.java.io :as io :refer [reader file]]
	    [clojure.string :as s :refer [trim split join]]
	    [clj-time.format :as tformat :refer [formatter parse unparse]]))

(def data-columns [:last_name :first_name :gender :favorite_color :dob])
(def record-delimiters (re-pattern "\\s[|]\\s|[,]\\s|\\s"))
(def date-format  "MM/dd/YYYY")
(def default-delimiter ", ")
(def s-configs {:gender {:order :asc :sym-key :gender}
		   :lastname {:order :asc :sym-key :last_name}
		   :dob {:order :asc :sym-key :dob}
		   :lastname-desc {:order :asc :sym-key :last_name}})

(defn parse-date 
  "Calls external lib to parse give date string"
  [date]
  (tformat/parse (tformat/formatter date-format) date))

(defn unparse-date 
  "Converts external lib DateTime class to string"
  [date]
  (tformat/unparse (tformat/formatter date-format) date))

(defn format-date 
  "Updates :dob value in a map by calling given func on it"
  [f record]
  (update record :dob #(f %)))

(defn set-record-date 
  "Convenience method for parsing + updating :dob in a given hash. Public api"
  [record]
  (format-date parse-date record)) 
  
(defn assign-keys 
  "Splits string on delimiter, zips to map and appends to given result vector"
  [result line]
  (->> (s/split (s/trim line) record-delimiters) 
    (zipmap data-columns)
    (set-record-date) 
    (conj result)))

(defn cli-format 
  "Unparses time format & extracts values from records to produce delimited string for printing"
  [record] 
  (->> record
    (format-date unparse-date)
    (vals)
    (s/join default-delimiter)))

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

(defn sort-configs [] s-configs)

