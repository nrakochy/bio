(ns bio.handlers
    (:require [re-frame.core :as re-frame]
	      [ajax.core :refer [GET]]
              [bio.db :as db]))

(def base-url "http://localhost:3000/records")

(defn get-records [url]
   (GET url {:handler #(re-frame/dispatch [:sorted-records %1])
	     :response-format :json
	     :keywords? true})) 

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :get-dob-sort
 (fn  [db _]
   (get-records (str base-url "/birthdate"))))

(re-frame/register-handler
 :get-gender-sort
 (fn  [db _]
   (get-records (str base-url "/gender"))))

(re-frame/register-handler
 :get-name-sort
 (fn  [db _]
   (get-records (str base-url "/name"))))

(re-frame/register-handler
 :sorted-records
 (fn  [db [_ response]]
    (.log js/console (js->clj response))
    (assoc db/default-db :name (js->clj response :keywordize-keys true)))) 
   

