(ns megafono_feed.views.channels
  (:require [megafono_feed.views.layout :as layout]
            [megafono-feed.views.helpers.rss :as rss]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            [ring.util.response :as r]
            [clojure.tools.logging :as log]))

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

(defn show [channel slug]
  (log/info (:categories channel))
  (let [owner (first (:channel_ownerships channel))]
    (if channel
                (if (= slug (:slug channel))
                  (r/response (rss/feed channel owner))
                  (r/redirect (:slug channel) :moved-permanently)
                )
                (r/not-found ""))))
