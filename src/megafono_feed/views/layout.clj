(ns megafono-feed.views.layout
  (:require [hiccup.page :as h]))

(defn common [title & body]
  (h/html5
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content
                "width=device-width, initial-scale=1, maximum-scale=1"}]
      [:title title]]
    [:body
      [:div {:id "header"}
        [:h1 {:class "container"} "Megafono Feed"]]
      [:div {:id "content" :class "container"}
         body]]))

(defn four-oh-four []
  (common "Page Not Found"
          [:div {:id "four-oh-four"}
           "The page you requested could not be found"]))
