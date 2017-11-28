(ns megafono-feed.views.helpers.url
  (:require [hashids.core :as id]
            [clojure.string :refer [join]]))

(def megafono-host
  (System/getenv "MEGAFONO_HOST"))

(def megafono-site-endpoint
  (apply str ["https://www." megafono-host "/podcast"]))

(def megafono-feed-endpoint
  (apply str ["https://feed." megafono-host]))

(defn cdn-endpoint [id]
  (apply str ["https://" id ".cloudfront.net"]))

(def image-cdn-endpoint (cdn-endpoint "d17choic6g575e"))
(def episode-cdn-endpoint (cdn-endpoint "d1r52u2wzcgg4y"))

(def hashids-opts {:salt "MEGAFONO SHORTEN"})
(defn build-episode-url [episode]
  (apply str ["http://mfn.bz/" (id/encode hashids-opts (:nid episode))]))

(defn build-site-url [slug]
  (apply str [megafono-site-endpoint "/" slug]))

(defn buid-xml-url [slug]
  (apply str [megafono-feed-endpoint "/" slug]))

(defn build-image-url
  ([id artwork]
    (build-image-url "channel" "artwork" id artwork))
  ([model mounted-as id filename]
    (join "/" [image-cdn-endpoint model mounted-as id filename])))

(defn build-episode-image-url [episode channel]
  (if (nil? (:artwork episode))
        (build-image-url "channel" "artwork" (:id channel) (:artwork channel))
        (build-image-url "episode" "artwork" (:id episode) (:artwork episode))))

(defn- external-provider? [episode]
  (= "external" (:media_provider episode)))

(defn- goodbye-manual-feed? [channel]
  (= "goodbye-manual-feed" (str (:plan_uid (first (:subscriptions channel))))))

(defn build-episode-embed-url [episode channel]
  (join "/" [megafono-site-endpoint (:slug channel) "e" (:slug episode)]))

(defn build-episode-media-url [episode]
  (if (external-provider? episode)
    (if goodbye-manual-feed? (:media_url episode) "foog")
    (
     apply str [
                (join "/" [
                          episode-cdn-endpoint
                          (:id episode)
                          (or (:media_uploaded episode) "audio.mp3")
                          ])
                "?source=feed"
                ])))
