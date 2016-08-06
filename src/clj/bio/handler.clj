(ns bio.handler
  (:require [bio.core :refer [get-records update-records]]
	    [compojure.core :refer [GET POST defroutes context]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (context "/records" []
    (POST "/" [req] (update-records req))
    (GET "/gender" [] (get-records :gender))
    (GET "/birthdate" [] (get-records :birthdate))
    (GET "/name" [] (get-records :name)))
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload wrap-params))

(def handler (-> routes wrap-params))
