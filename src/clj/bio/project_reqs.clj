(ns bio.project-reqs
  (:require [bio.data-processing :as dp :refer [multisort sort-configs]]
	    [bio.file-io :as fio :refer [extract-records print-records]]))

(defn set-sort-requirements 
  "Preconfigured sort profiles for printing out"
  [config]
  (let [{:keys [gender lastname dob lastname-desc]} (config)]
    [[gender lastname] [dob] [lastname-desc]]))

(defn sort-print 
  "Sorts and prints records with a separator between each sort configuration"
  [coll sort-config]
  (let [separator "____________________________"]
  (prn separator)
  (fio/print-records (dp/multisort coll sort-config))
  (prn separator)))

(defn output-reqs 
  "Runs output requirements (headless)"
  [coll]
  (dorun (map #(sort-print coll %) (set-sort-requirements dp/sort-configs))))

(defn -main [& paths]
  (output-reqs (fio/extract-records paths)))
