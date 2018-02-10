(ns megafono-feed.views.helpers.url
  (:require [hashids.core :as id]
            [clojure.string :refer [join]]))

(defn- cdn-endpoint [id] (apply str ["https://" id ".cloudfront.net"]))
(defn- to-url [parts] (join "/" (map str parts)))
(defn- goodbye-manual-feed? [channel]
  (= "goodbye-manual-feed" (str (:plan_uid (first (:subscriptions channel))))))

(def megafono-host "megafono.host")
(def soundcloud-client-id (System/getenv "SI_CLIENT_ID"))
(def megafono-site-endpoint (apply str ["https://www." megafono-host "/podcast"]))
(def megafono-feed-endpoint (apply str ["https://feed." megafono-host]))
(def image-cdn-endpoint (cdn-endpoint "d17choic6g575e"))
(def episode-cdn-endpoint (cdn-endpoint "d1r52u2wzcgg4y"))
(def hashids-opts {:salt "MEGAFONO SHORTEN"})

(defn build-episode-url [episode]
  (to-url ["http://mfn.bz" (id/encode hashids-opts (:nid episode))]))

(defn build-site-url [slug]
  (to-url [megafono-site-endpoint slug]))

(defn buid-xml-url [slug]
  (to-url [megafono-feed-endpoint slug]))

(defn build-image-url
  ([id artwork]
    (build-image-url "channel" "artwork" id artwork))
  ([model mounted-as id filename]
    (to-url [image-cdn-endpoint model mounted-as id filename])))

(defn build-episode-image-url [episode channel]
  (if (nil? (:artwork episode))
        (build-image-url "channel" "artwork" (:id channel) (:artwork channel))
        (build-image-url "episode" "artwork" (:id episode) (:artwork episode))))

(defn build-episode-embed-url [episode channel]
  (to-url [megafono-site-endpoint (:slug channel) "e" (:slug episode)]))

(defn build-episode-media-url [episode channel]
  (case (:media_provider episode)
    "external" (if (goodbye-manual-feed? channel)
                 (:media_url episode)
                 (apply str [
                              (to-url [megafono-site-endpoint
                                       (:slug channel)
                                       (apply str [(:slug episode) ".mp3"])])
                              "?source=feed"]))

    "s3" (apply str [
                      (to-url [episode-cdn-endpoint
                               (:id episode)
                               (or (:media_uploaded episode) "audio.mp3")])
                      "?source=feed"])

    "soundcloud" (apply str [
                              (:media_url episode)
                              "?client_id=" soundcloud-client-id])))

