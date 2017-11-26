(ns megafono_feed.controllers.channels
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.string :as str]
            [ring.util.response :as r]
            [megafono_feed.views.channels :as view]
            [megafono_feed.models.channel :as channel]))

(defn index []
  (view/index (channel/all)))

(defn show [slug]
  (-> (view/show (channel/find-by-slug slug) slug)
      (r/header "Content-Type" "application/xml; charset=utf-8")))

(defroutes routes
  (GET "/" [] (index))
  (GET "/favicon.ico" [] "")
  (GET "/serviceworker.js" [] "")
  (GET "/:slug" [slug] (show slug)))
