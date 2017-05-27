(ns megafono-feed.views.helpers.url)

(defn build-site-url [slug]
  (apply str ["https://www.megafono.io/podcast/" slug]))

(defn buid-xml-url [slug]
  (apply str ["https://feed.megafono.io/" slug]))

(defn build-image-url [id artwork]
  (apply str ["https://d17choic6g575e.cloudfront.net/channel/artwork/" id "/" artwork]))
