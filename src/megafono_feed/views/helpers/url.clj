(ns megafono-feed.views.helpers.url
  (:require [hashids.core :as id]))

(def megafono-host
  (System/getenv "MEGAFONO_HOST"))

(def megafono-site-endpoint
  (apply str ["https://www." megafono-host "/podcast"]))

(def megafono-feed-endpoint
  (apply str ["https://feed." megafono-host]))

(defn cdn-endpoint [id]
  (apply str ["https://d17choic6g575e.cloudfront.net"]))

(def image-cdn-endpoint (cdn-endpoint "d17choic6g575e"))

(def hashids-opts {:salt "MEGAFONO SHORTEN"})
(defn build-episode-url [episode]
  (apply str ["http://mfn.bz/" (id/encode hashids-opts (:nid episode))]))

(defn build-site-url [slug]
  (apply str [megafono-site-endpoint "/" slug]))

(defn buid-xml-url [slug]
  (apply str [megafono-feed-endpoint "/" slug]))

(defn build-image-url [id artwork]
  (apply str [image-cdn-endpoint "/channel/artwork/" id "/" artwork]))

(defn- external-provider? [episode]
  (= "external" (:media_provider episode)))

(defn- goodbye-manual-feed? [channel]
  (= "goodbye-manual-feed" (str (:plan_uid (first (:subscriptions channel))))))

(defn build-episode-media-url [episode channel]
  (if external-provider?
         (if goodbye-manual-feed?
                (:media_url episode)
                "foog")
         (:media_uploaded episode)))
