(ns megafono-feed.views.helpers.url
  (:require [hashids.core :as id]))

(def hashids-opts {:salt "MEGAFONO SHORTEN"})

(defn build-site-url [slug]
  (apply str ["https://www.megafono.io/podcast/" slug]))

(defn build-episode-url [episode]
  (apply str ["http://mfn.bz/" (id/encode hashids-opts (:nid episode))]))

(defn buid-xml-url [slug]
  (apply str ["https://feed.megafono.io/" slug]))

(defn build-image-url [id artwork]
  (apply str ["https://d17choic6g575e.cloudfront.net/channel/artwork/" id "/" artwork]))
