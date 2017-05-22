(ns megafono-feed.views.helpers.rss
  (:use [clojure.xml :only [emit]])
  (:require [megafono-feed.views.helpers.time :as time]
            [megafono-feed.views.helpers.xml :as xml]
            [megafono-feed.views.helpers.url :as url]))

(defn item-build [site author {:keys [id title content time]}]
  (let [link (str site "/" id )]
    (xml/tag :item nil
         (xml/tag :guid nil link)
         (xml/tag :title nil title)
         (xml/tag :dc:creator nil author)
         (xml/tag :description nil content)
         (xml/tag :link nil link)
         (xml/tag :pubDate nil (time/format-rss time))
         (xml/tag :category nil "clojure"))))

(defn channel-build [channel posts]
  (let [created_at (time/format-rss (:updated_at channel))]
    (let [title (:name channel)]
      (xml/tag :rss {:version "2.0"
                 :xmlns:dc "http://purl.org/dc/elements/1.1/"
                 :xmlns:sy "http://purl.org/rss/1.0/modules/syndication/"
                 :xmlns:itunes "http://www.itunes.com/dtds/podcast-1.0.dtd"
                 :xmlns:atom "http://www.w3.org/2005/Atom"
                 :xmlns:content "http://purl.org/rss/1.0/modules/content/"
                 :xmlns:rawvoice "http://www.rawvoice.com/rawvoiceRssModule/"}
           ;(update-in
           (xml/tag :channel nil
                (xml/tag :title nil title)
                (xml/tag :atom:link {:href (url/buid-xml-url (:slug channel)) :rel "self" :type "application/rss+xml"})
                (xml/tag :link nil (url/build-site-url (:slug channel)))
                (xml/tag :description nil (xml/strip-html (:body channel)))
                (xml/tag :lastBuildDate nil created_at)
                (xml/tag :pubDate nil created_at)
                (xml/tag :language nil (:language channel))
                (xml/tag :sy:updatePeriod nil "hourly")
                (xml/tag :sy:updateFrequency nil "1")
                (xml/tag :generator nil "Megafono Feed (+https://www.megafono.io/>")
                (xml/tag :itunes:summary nil (xml/strip-html (:body channel)))
                (xml/tag :itunes:author nil "TODO: Emerson Almeida")
                (xml/tag :itunes:explicit nil "TODO" )
                (xml/tag :itunes:image {:href "TODO"})
                (xml/tag :itunes:owner nil
                     (xml/tag :itunes:name nil "TODO: name")
                     (xml/tag :itunes:email nil "TODO: email"))
                ;(tag :managingEditor>emerson@megafono.io (Emerson Almeida)</managingEditor>
                (xml/tag :itunes:subtitle nil (:subtitle channel))
                ;(tag :image>...</image>
                ;(tag :itunes:category text="Technology">...</itunes:category>
                (xml/tag :copyright nil (str (:copyright channel)))
                (xml/tag :dc:title nil title)
                (xml/tag :dc:description nil (xml/strip-html (:body channel)))
                ;(tag :dc:creator>Emerson Almeida</dc:creator>
                (xml/tag :webMaster nil "webmaster@megafono.io (Megafono)" )
                ;  [:content]
                ;  into (map (partial item site author) posts)
                )))))

(defn feed [channel posts]
  (with-out-str (emit (channel-build channel posts))))
