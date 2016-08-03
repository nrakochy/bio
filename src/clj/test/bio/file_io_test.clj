(ns bio.file-io-test
  (:require [clojure.test :refer :all]
            [bio.file-io :refer :all]))

(deftest fn-assign-keys
  (let [result (zipmap data-columns ["LastName" "FirstName" "Gender" "Color" "DOB"])]
  (testing "Assigns keys to a pipe delimited string" 
      (let [example "LastName | FirstName | Gender | Color | DOB"]  
      (is (= result (assign-keys example)))))
  (testing "Assigns keys to a space delimited string" 
      (let [example "LastName FirstName Gender Color DOB"]  
      (is (= result (assign-keys example)))))
  (testing "Assigns keys to a comma delimited string" 
      (let [example "LastName, FirstName, Gender, Color, DOB"]  
      (is (= result (assign-keys example)))))))
