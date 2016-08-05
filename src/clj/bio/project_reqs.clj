(ns bio.project-reqs
  (:require [bio.file-io :as fio :refer [extract-records multisort print-records]]))

(def gender-asc {:order :asc :sym-key :gender})
(def lastname-asc {:order :asc :sym-key :last_name})
(def dob-asc {:order :asc :sym-key :dob})
(def lastname-desc {:order :asc :sym-key :dob})
(def project-reqs [[gender-asc lastname-asc] [dob-asc] [lastname-desc]]) 

(defn sort-print [coll sort-config]
  (let [separator "____________________________"]
  (prn separator)
  (print-records (multisort coll sort-config))
  (prn separator)))

(defn output-reqs [coll]
  (dorun (map #(sort-print coll %) project-reqs)))

(defn run [& paths]
  (output-reqs (extract-records paths)))

