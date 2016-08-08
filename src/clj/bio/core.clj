(ns bio.core
  (:require [bio.file-io :as fio :refer [extract-records multisort set-record-date]]
	    [cheshire.custom :as json :refer [parse-string encode add-encoder encode-str]]))

(def sample-records-path "./resources/sample")
(def s-configs {:gender {:order :asc :sym-key :gender}
		   :lastname {:order :asc :sym-key :last_name}
		   :dob {:order :asc :sym-key :dob}
		   :lastname-desc {:order :asc :sym-key :last_name}})
(def default-sort :gender)

(defn init-records [& paths]
  (extract-records paths))
 
(def records (atom (init-records sample-records-path)))

(defn encode-datetime []
  (json/add-encoder org.joda.time.DateTime json/encode-str))

(defn sort-configs [] s-configs)

(defn get-records [sort-type]
 (memoize (encode-datetime))
 (json/encode (multisort @records [(sort-type s-configs)])))

(defn add-record [record]
  (swap! records conj record))

(defn update-records [params]
   (let [params (first (json/parse-string params true))]
   (add-record (set-record-date params))
   (get-records default-sort)))
