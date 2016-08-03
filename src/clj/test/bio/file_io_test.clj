(ns bio.file-io-test
  (:require [clojure.test :refer :all]
	    [clojure.string :as s :refer [split]]
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
