(ns bio.views
    (:require [re-frame.core :as re-frame]))

(def sort-btns
  [[:a {:on-click #(re-frame/dispatch [:get-dob-sort]) :class "btn btn-default"} "Birthdate"]
  [:a {:on-click #(re-frame/dispatch [:get-gender-sort])  :class "btn btn-default"} "Gender"]
  [:a {:on-click #(re-frame/dispatch [:get-name-sort]) :class "btn btn-default"} "Last Name"]])

(defn record-panel [data]
  (let [{:keys [first_name last_name dob favorite_color gender]} data]
  [:div {:class "col-xs-6 col-md-4 col-lg-3"}
    [:div {:class "panel panel-primary"}
     [:div {:class "panel-heading"}
       [:h3 {:class "panel-title"} (str first_name " " last_name)]]
     [:div {:class "panel-body"}  (str dob " | " gender " | " favorite_color)]]]))

(defn records-layout []
  (let [records (re-frame/subscribe [:name])]
  (into [:div] (map record-panel @records))))  

(defn btn [component]
  component)

(defn btns-layout []
  (into [:div] (map btn sort-btns)))  
  
(defn main-panel []
  [:section {:class "col-md-12"}
   [:div {:class "container-fluid"}
    [:h1 "Bio - A Profiler"]
      [:div {:class "row-fluid"}
	(btns-layout)]
	[:br]
      [:div {:class "row-fluid"}
	(records-layout)]]])
