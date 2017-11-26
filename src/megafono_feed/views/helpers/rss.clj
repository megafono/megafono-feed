(ns megafono-feed.views.helpers.rss
  (:use [clojure.xml :only [emit]])
  (:require [megafono-feed.views.helpers.time :as time]
            [megafono-feed.views.helpers.xml :as xml]
            [megafono-feed.views.helpers.url :as url]))

(defn episode-build [channel owner episode]
  (let [link (str channel "/" (:id episode) )]
    (xml/tag :item nil
             (xml/tag :title nil (:title episode))
             (xml/tag :dc:title nil (:title episode))
             (xml/tag :link nil (url/build-episode-url episode))
             (xml/tag :pubDate nil (time/format-rss (:published_at episode)))
             (xml/tag :dc:creator nil (:name owner))
             (xml/tag :guid {:isPermarlink false} (str (or (:guid episode) (:id episode))))
             (xml/tag :description nil (xml/strip-html (:body episode)))
             (xml/tag :content:encoded nil (xml/strip-html (:body episode)))
             (xml/tag :dc:description nil (xml/strip-html (:body episode)))
             (xml/tag :enclosure {:url (url/build-episode-media-url episode channel)})
             (xml/tag :itunes:author nil (:name owner))
             ;; (xml/tag :itunes:image nil (:title episode))
             ;; (xml/tag :itunes:duration nil (:title episode))
             (xml/tag :itunes:explicit nil (if (= (:explict channel) 1) "Yes" "No") )
             (xml/tag :itunes:subtitle nil (:subtitle episode))
             (xml/tag :itunes:summary nil (xml/strip-html (:body episode)))
             ;; (xml/tag :rawvoice:poster nil (:title episode))
             ;; (xml/tag :rawvoice:embed nil (:title episode))
             )))

(defn channel-build [channel owner]
  (let [created_at (time/format-rss (:updated_at channel))]
    (let [title (:name channel)]
      (xml/tag :rss {:version "2.0"
                     :xmlns:itunes "http://www.itunes.com/dtds/podcast-1.0.dtd"
                     :xmlns:atom "http://www.w3.org/2005/Atom"
                     :xmlns:sy "http://purl.org/rss/1.0/modules/syndication/"
                     :xmlns:dc "http://purl.org/dc/elements/1.1/"
                     :xmlns:content "http://purl.org/rss/1.0/modules/content/"
                     :xmlns:rawvoice "http://www.rawvoice.com/rawvoiceRssModule/"}
               (update-in
                 (xml/tag :channel nil
                          (xml/tag :title nil title)
                          (xml/tag :atom:link {:href (url/buid-xml-url (:slug channel)) :rel "self" :type "application/rss+xml"})
                          (xml/tag :link nil (url/build-site-url (:slug channel)))
                          (xml/tag :description nil (xml/strip-html (:body channel)))
                          (xml/tag :lastBuildDate nil created_at)
                          (xml/tag :pubDate nil created_at)
                          (xml/tag :language nil (:language channel))
                          (xml/tag :sy:updatePeriod nil "daily")
                          (xml/tag :sy:updateFrequency nil "1")
                          (xml/tag :generator nil "Megafono Feed v1.0.0 (+https://www.megafono.io/)")
                          (xml/tag :itunes:summary nil (xml/strip-html (:body channel)))
                          (xml/tag :itunes:type nil (:submission_type channel))
                          (xml/tag :itunes:author nil (:name owner))
                          (xml/tag :itunes:explicit nil (if (= (:rating channel) 1) "Yes" "No") )
                          (xml/tag :itunes:image {:href (url/build-image-url (:id channel) (:artwork channel))})
                          (xml/tag :itunes:owner nil
                                   (xml/tag :itunes:name nil (:name owner))
                                   (xml/tag :itunes:email nil (:email owner)))
                          (xml/tag :managingEditor nil "emerson@megafono.io (Emerson Almeida)")
                          (xml/tag :itunes:subtitle nil (:subtitle channel))
                          (xml/tag :image nil
                                   (xml/tag :title nil title)
                                   (xml/tag :url nil (url/build-image-url (:id channel) (:artwork channel)))
                                   (xml/tag :link nil (url/build-site-url (:slug channel))))
                          ;; (tag :itunes:category text="Technology">...</itunes:category>
                          ;; (map (fn [category] (xml/tag :x nil (:name category))) (:categories channel))
                          ;; channel sq (seq (:categories channel))
                          ;; (log/info (map (fn [category] (:name category)) (:categories channel)))
                          (xml/tag :copyright nil (str (:copyright channel)))
                          (xml/tag :dc:title nil title)
                          (xml/tag :dc:description nil (xml/strip-html (:body channel)))
                          (xml/tag :dc:creator nil (:name owner))
                          (xml/tag :webMaster nil "webmaster@megafono.io (Megafono)"))
                 [:content]
                 into (map (partial episode-build channel owner) (:episodes channel)))))))

(defn feed [channel owner]
  (with-out-str (emit (channel-build channel owner))))
