(ns bio.handler-test
  (:require [clojure.test :refer :all]
            [bio.handler :as router :refer [retrieve-records handler]]
	    [ring.mock.request :as mock]))

(deftest fn-handler-routes
  (testing "GET routes"
    (let [response {:status 200 :headers {"Content-Type" "application/json"}}]
    (let [name-test (select-keys (router/handler (mock/request :get "/records/name"))[:status :headers])]
    (let [gender-test (select-keys (router/handler (mock/request :get "/records/gender"))[:status :headers])]
    (let [dob-test (select-keys (router/handler (mock/request :get "/records/birthdate"))[:status :headers])]
      (is (= response gender-test)) 
      (is (= response dob-test)) 
      (is (= response name-test)))))))
  (testing "POST routes"
    (let [response {:status 200 :headers {"Content-Type" "application/json"}}]
    (let [name-test (select-keys (router/handler (mock/request :get "/records/name"))[:status :headers])]
      (is (= response name-test))))))
