(ns bio.core-test
  (:require [clojure.test :refer :all]
            [bio.core :as router :refer :all])) 
(comment
(deftest fn-handler-routes
  (testing "GET routes"
    (let [response {:status 200 :headers {"Content-Type" "application/json"}}]
    (is (= response name-test)))))
)
