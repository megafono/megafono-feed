(ns megafono_feed.views.channels
  (:use [clojure.xml :only [emit]])
  (:import java.util.Date)
  (:require [megafono_feed.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :as anti-forgery]
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



(defn format-time [time]
  (.format (new java.text.SimpleDateFormat
                "EEE, dd MMM yyyy HH:mm:ss ZZZZ") time))

(defmacro tag [id attrs & content]
  `{:tag ~id :attrs ~attrs :content [~@content]})

(defn item [site author {:keys [id title content time]}]
  (let [link (str site "/" id )]
    (tag :item nil
         (tag :guid nil link)
         (tag :title nil title)
         (tag :dc:creator nil author)
         (tag :description nil content)
         (tag :link nil link)
         (tag :pubDate nil (format-time time))
         (tag :category nil "clojure"))))

(defn build-site-url [slug]
   (apply str ["https://www.megafono.io/podcast/" slug]))

(defn buid-xml-url [slug]
  (apply str ["https://feed.megafono.io/" slug]))

(defn message [site title author channel posts]
  (let [date (format-time (new Date))]
    (tag :rss {:version "2.0"
               :xmlns:dc "http://purl.org/dc/elements/1.1/"
               :xmlns:sy "http://purl.org/rss/1.0/modules/syndication/"
               :xmlns:itunes "http://www.itunes.com/dtds/podcast-1.0.dtd"
               :xmlns:atom "http://www.w3.org/2005/Atom"
               :xmlns:content "http://purl.org/rss/1.0/modules/content/"
               :xmlns:rawvoice "http://www.rawvoice.com/rawvoiceRssModule/"}
         (update-in
           (tag :channel nil
                (tag :title nil (:name channel))
                (tag :atom:link {:href (buid-xml-url (:slug channel)) } "")
                (tag :description nil (:body channel))
                (tag :link nil (build-site-url (:slug channel)))
                (tag :lastBuildDate nil date)
                (tag :dc:creator nil author)
                (tag :language nil "en-US")
                (tag :sy:updatePeriod nil "hourly")
                (tag :sy:updateFrequency nil "1"))
           [:content]
           into (map (partial item site author) posts)))))

(defn rss-feed [site title author channel posts]
  (with-out-str (emit (message site title author channel posts))))

(defn show [channel]
  (log/info channel)
  (rss-feed (build-site-url (:slug channel)) (:name channel) "My blog" channel
         [{:id 1
           :title "Test post"
           :content "Some content"
           :time (new Date)}]))
