(ns bio.file-io
  (:require [clojure.java.io :as io :refer [reader file]])
  (:require [clojure.string :as s :refer [split]]))

(def data-columns [:last_name :first_name :gender :favorite_color :dob])
(def record-delimiter (re-pattern "\\s[|]\\s|[,]\\s|\\s"))

(defn assign-keys [line]
  (->> (s/split line record-delimiter) 
    (zipmap data-columns)))

(defn extract-records [file]
  (with-open [r (io/reader file)]
    (->> (line-seq r)
	 (map assign-keys))))
