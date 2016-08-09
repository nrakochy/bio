(ns bio.file-io-test
  (:require [clojure.test :refer :all]
	    [clojure.string :as s :refer [split]]
	    [clojure.java.io :as io :refer [as-file file]]
	    [clj-time.format :as tformat :refer [formatter parse]]
            [bio.file-io :refer :all]))

(deftest fn-extract-records
  (testing "Correctly filters non-files from given path and calls reduce method"
    (with-redefs [parse-file (fn [data] data)] 
      (let [file '("fake-file.txt")]
      (let [result (lazy-seq '())]
      (is (= result (extract-records file))))))))
