(ns bio.handler
  (:require [bio.core :as repo :refer [get-records update-records]]
	    [compojure.core :refer [GET POST defroutes context]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.reload :refer [wrap-reload]]))


(defn retrieve-records [arg] (repo/get-records arg))

(defn respond [body]
  {:status 200 :headers {"Content-Type" "application/json"} :body body})

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (context "/records" []
    (POST "/"  {body :body} (respond (repo/update-records (slurp body))))
    (GET "/gender" [] (respond (repo/get-records :gender)))
    (GET "/birthdate" [] (respond (repo/get-records :dob)))
    (GET "/name" [] (respond (retrieve-records :lastname))))
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload wrap-json-body))

(def handler (-> routes wrap-json-body))
