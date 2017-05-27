(ns megafono_feed.views.channels
  (:require [megafono_feed.views.layout :as layout]
            [megafono-feed.views.helpers.rss :as rss]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :as anti-forgery]
            [clojure.tools.logging :as log])
  (:import (java.util Date)))

(defn display-channels [channels]
  [:ul
   (map
     (fn [channel] [:li
                    [:a {:href (apply str ["/" (:slug channel)])}
                     (h (:name channel))
                     ]
                    ])
     channels)])

(defn index [channels]
  (layout/common "Megafono Feed"
                 [:div]
                 (display-channels channels)))

(defn show [channel]
  (let [owner (first (:channel_ownerships channel))]
    (if channel (rss/feed channel owner)
                (ring.util.response/not-found "channel not found"))))
