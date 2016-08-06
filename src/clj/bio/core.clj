(ns bio.core)

(def sort-configs {:gender {:order :asc :sym-key :gender}
		   :lastname {:order :asc :sym-key :last_name}
		   :dob {:order :asc :sym-key :dob}
		   :lastname-desc {:order :asc :sym-key :last_name}})

(defn get-records [sort-type]
  (str "RETURNED" sort-type))
  
(defn update-records [req]
  (str "REQUEST: " req))
 

