(ns megafono_feed.core
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [megafono_feed.controllers.channels :as channels]
            [megafono_feed.views.layout :as layout])
  (:gen-class))

(defroutes routes
  channels/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def application (wrap-defaults routes site-defaults))

(defn start [port]
  (ring/run-jetty application {:port port
                               :join? false}))
(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
