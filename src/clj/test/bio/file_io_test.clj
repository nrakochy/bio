(ns bio.file-io-test
  (:require [clojure.test :refer :all]
	    [clojure.string :as s :refer [split]]
	    [clojure.java.io :as io :refer [as-file file]]
            [bio.file-io :refer :all]))

(deftest def-record-delimiter 
  (let [result ["LastName" "FirstName" "Gender" "Color" "DOB"]]  
  (testing "Regex correctly splits on comma" 
      (let [comma "LastName, FirstName, Gender, Color, DOB"]  
      (is (= result (s/split comma record-delimiter)))))
  (testing "Regex correctly splits on space" 
      (let [space "LastName FirstName Gender Color DOB"]  
      (is (= result (s/split space record-delimiter)))))
  (testing "Regex correctly splits on pipe" 
      (let [pipe "LastName | FirstName | Gender | Color | DOB"]  
      (is (= result (s/split pipe record-delimiter)))))))

(deftest fn-assign-keys
  (let [result (conj [] (zipmap data-columns ["LastName" "FirstName" "Gender" "Color" "DOB"]))]
  (testing "Assigns keys to a pipe delimited string" 
      (let [example "LastName | FirstName | Gender | Color | DOB"]  
      (is (= result (assign-keys [] example)))))
  (testing "Assigns keys to a space delimited string" 
      (let [example "LastName FirstName Gender Color DOB"]  
      (is (= result (assign-keys [] example)))))
  (testing "Assigns keys to a comma delimited string" 
      (let [example "LastName, FirstName, Gender, Color, DOB"]  
      (is (= result (assign-keys [] example)))))))

(deftest fn-extract-records
  (testing "Correctly filters non-files from given path and calls reduce method"
    (with-redefs [parse-file (fn [data] data)] 
      (let [file (as-file "fake-file.txt")]
      (let [result (lazy-seq '())]
      (is (= result (extract-records file))))))))

(deftest fn-configurable-sort
  (let [example1 {:test1 1 :test2 1}]   
  (let [example2 {:test1 2 :test2 2}]   
  (testing "Desc sort based on key"
      (let [sort-config {:order :desc :sym-key :test1}]   
      (is (= 1 (configurable-sort sort-config example1 example2)))))
  (testing "Asc sort based on key"
      (let [sort-config {:order :asc :sym-key :test1}]   
      (is (= -1 (configurable-sort sort-config example1 example2))))))))

(deftest fn-kv-sort
  (let [example1 {:test1 1 :test2 1}]   
  (let [example2 {:test1 1 :test2 2}]   
  (let [example3 {:test1 2 :test2 2}]   
  (testing "Returns zero if search configs are equal"
      (let [sort-configs [{:order :desc :sym-key :test1}]]   
      (is (= 0 (kv-sort sort-configs example1 example2)))))
  (testing "Sorts on second config if 1st sort-config finds equality"
      (let [sort-configs [{:order :desc :sym-key :test1} {:order :asc :sym-key :test2}]]   
      (is (= -1 (kv-sort sort-configs example1 example2)))))
  (testing "Short-circuits if sorting on first config finds inequality" 
      (let [sort-configs [{:order :desc :sym-key :test2} {:order :asc :sym-key :test1}]]   
      (is (= 1 (kv-sort sort-configs example1 example2)))))))))
  
(deftest fn-multisort-simple 
  (let [example-recs [{:test1 "AAAA" :test2 "BBBB" :test3 "CCCC"} {:test1 "BBBB" :test2 "CCCC" :test3 "AAAA"} {:test1 "CCCC" :test2 "AAAA" :test3 "BBBB"} {:test1 "DDDD" :test2 "DDDD" :test3 "BBBB"}]]  
  (testing "Desc sort, single symbol"
      (let [desc-config [{:order :desc :sym-key :test1}]] 
	(is (= (reverse example-recs) (multisort example-recs desc-config))))
      (let [desc-config2 [{:order :desc :sym-key :test2}]] 
      (let [result [{:test1 "DDDD" :test2 "DDDD" :test3 "BBBB"} {:test1 "BBBB" :test2 "CCCC" :test3 "AAAA"} {:test1 "AAAA" :test2 "BBBB" :test3 "CCCC"} {:test1 "CCCC" :test2 "AAAA" :test3 "BBBB"}]]
	(is (= result (multisort example-recs desc-config2))))))
  (testing "Asc sort, single symbol"
      (let [asc-config [{:order :asc :sym-key :test3}]] 
      (let [result [{:test1 "BBBB" :test2 "CCCC" :test3 "AAAA"} {:test1 "CCCC" :test2 "AAAA" :test3 "BBBB"} {:test1 "DDDD" :test2 "DDDD" :test3 "BBBB"} {:test1 "AAAA" :test2 "BBBB" :test3 "CCCC"}]]
	(is (= result (multisort example-recs asc-config))))))))

(deftest fn-multisort-complex 
  (let [example-recs [{:test1 "AAAA" :test2 "BBBB" :test3 "CCCC"} {:test1 "BBBB" :test2 "CCCC" :test3 "AAAA"} {:test1 "CCCC" :test2 "AAAA" :test3 "BBBB"} {:test1 "DDDD" :test2 "DDDD" :test3 "BBBB"}]]  
  (testing "Multiple sort configs" 
      (let [desc-config {:order :asc :sym-key :test3}] 
      (let [asc-config {:order :asc :sym-key :test2}] 
      (let [sort-config [desc-config asc-config]]
      (let [result [{:test1 "BBBB" :test2 "CCCC" :test3 "AAAA"} {:test1 "CCCC" :test2 "AAAA" :test3 "BBBB"} {:test1 "DDDD" :test2 "DDDD" :test3 "BBBB"} {:test1 "AAAA" :test2 "BBBB" :test3 "CCCC"}]]
	(is (= result (multisort example-recs sort-config))))))))))
