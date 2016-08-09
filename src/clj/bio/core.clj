(ns bio.core
  (:require [bio.data-processing :as dp :refer [multisort set-record-date sort-configs]]
	    [bio.file-io :as fio :refer [extract-records]]
	    [cheshire.custom :as json :refer [parse-string encode add-encoder encode-str]]))

;; Initialization
(def sample-records-path "./resources/sample")
(def default-sort :gender)

(defn init-records [& paths]
  (fio/extract-records paths))
 
(def records (atom (init-records sample-records-path)))

;; Data Processing 
(defn encode-datetime []
  (json/add-encoder org.joda.time.DateTime json/encode-str))

(defn get-records [sort-type]
 (memoize (encode-datetime))
 (json/encode (dp/multisort @records [(sort-type (dp/sort-configs))])))

(defn add-record [record]
  (swap! records conj record))

(defn update-records [params]
   (let [params (first (json/parse-string params true))]
   (add-record (dp/set-record-date params))
   (get-records default-sort)))
