(ns megafono_feed.controllers.channels
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [megafono_feed.views.channels :as view]
            [megafono_feed.models.channel :as model]))

(defn index []
  (view/index (model/all)))

(defn show [slug]
  (view/show (model/find-by-slug slug)))

(defroutes routes
           (GET "/" [] (index))
           (GET "/favicon.ico" [] "")
           (GET "/:slug" [slug] (show slug)))
