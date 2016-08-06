(ns bio.project-reqs
  (:require [bio.file-io :as fio :refer [extract-records multisort print-records]]
	    [bio.core :refer [sort-configs]]))

(defn required-sort [config]
  (let [{:keys [gender lastname dob lastname-desc]} config]
    [[gender lastname] [dob] [lastname-desc]]))

(defn sort-print [coll sort-config]
  (let [separator "____________________________"]
  (prn separator)
  (fio/print-records (fio/multisort coll sort-config))
  (prn separator)))

(defn output-reqs [coll]
  (dorun (map #(sort-print coll %) (required-sort sort-configs))))

(defn run [& paths]
  (output-reqs (fio/extract-records paths)))

