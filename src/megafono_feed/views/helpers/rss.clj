(ns megafono-feed.views.helpers.rss
  (:use [clojure.data.xml :only [emit-str cdata]])
  (:require [megafono-feed.views.helpers.time :as time]
            [megafono-feed.views.helpers.xml :as xml]
            [megafono-feed.views.helpers.url :as url]
            [clj-time.coerce :refer [from-long]]
            [clj-time.format :refer [unparse formatter]]))

(defn episode-build [channel owner episode]
  (let [link (str channel "/" (:id episode))]
    (xml/tag :item nil
             (xml/tag :title nil (:title episode))
             (xml/tag :dc:title nil (:title episode))
             (xml/tag :link nil (url/build-episode-url episode))
             (xml/tag :pubDate nil (time/format-rss (:published_at episode)))
             (xml/tag :dc:creator nil (cdata (:owner_name channel)))
             (xml/tag :guid {:isPermarlink false} (str (or (:guid episode) (:id episode))))
             (xml/tag :description nil (cdata (:body episode)))
             (xml/tag :content:encoded nil (str (:body episode)))
             (xml/tag :dc:description nil (xml/strip-html (:body episode)))
             (xml/tag :enclosure {:url (url/build-episode-media-url episode channel) :length (:media_length episode) :type (:media_content_type episode)})
             (xml/tag :itunes:author nil (:owner_name channel))
             (xml/tag :itunes:image {:url (url/build-episode-image-url episode channel)})
             (if (zero? (:season episode)) "" (xml/tag :itunes:season nil (:season episode)))
             (if (zero? (:number episode)) "" (xml/tag :itunes:episode nil (:number episode)))
             (xml/tag :itunes:episodeType nil (:submission_type episode))
             (xml/tag :itunes:duration nil (if (nil? (:duration episode))
                                             nil
                                             (unparse (formatter "HH:mm:ss") (from-long (long (* 1000 (:duration episode)))))))
             (xml/tag :itunes:explicit nil (if (= (:explict channel) 1) "Yes" "No") )
             (xml/tag :itunes:subtitle nil (:subtitle episode))
             (xml/tag :itunes:summary nil (xml/strip-html (:body episode)))
             (xml/tag :rawvoice:poster {:url (url/build-episode-image-url episode channel)})
             (xml/tag :rawvoice:embed nil (apply str [
                                                      "<iframe src=\""
                                                      (url/build-episode-embed-url episode channel)
                                                      "\" frameborder=\"0\" allowtransparency=\"true\"></iframe>"])))))

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
                          (xml/tag :itunes:author nil (:owner_name channel))
                          (xml/tag :itunes:explicit nil (if (= (:rating channel) 1) "Yes" "No") )
                          (xml/tag :itunes:image {:href (url/build-image-url (:id channel) (:artwork channel))})
                          (xml/tag :itunes:owner nil
                                   (xml/tag :itunes:name nil (:owner_name channel))
                                   (xml/tag :itunes:email nil (:owner_email channel)))
                          (xml/tag :managingEditor nil (apply str [(:owner_email channel) " (" (:owner_name channel) ")"]))
                          (xml/tag :itunes:subtitle nil (:subtitle channel))
                          (xml/tag :image nil
                                   (xml/tag :title nil title)
                                   (xml/tag :url nil (url/build-image-url (:id channel) (:artwork channel)))
                                   (xml/tag :link nil (url/build-site-url (:slug channel))))
                          (xml/tag :copyright nil (str (:copyright channel)))
                          (xml/tag :dc:title nil title)
                          (xml/tag :dc:description nil (cdata (:body channel)))
                          (xml/tag :dc:creator nil (:owner_name channel))
                          (xml/tag :webMaster nil "support@megafono.host (Megafono)"))
                 [:content]
                 into (concat (xml/categories-tag (:categories channel)) (map (partial episode-build channel owner) (:episodes channel))))))))

(defn feed [channel owner]
  (emit-str (channel-build channel owner)))
