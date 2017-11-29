(ns megafono-feed.core
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [megafono-feed.controllers.channels :as channels]
            [megafono-feed.views.layout :as layout]
            [environ.core :refer [env]])
  (:gen-class))

(defroutes routes
           channels/routes
           (route/resources "/")
           (route/not-found (layout/four-oh-four)))

(def application (wrap-defaults routes site-defaults))

(defn start [port]
  (ring/run-jetty application {:port port
                               :join? false}))
(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (start port)))
