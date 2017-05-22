(ns megafono-feed.views.helpers.xml
  (:require
    [jsoup.soup :as soup]))

(defmacro tag [id attrs & content]
  `{:tag ~id :attrs ~attrs :content [~@content]})

(defn strip-html
  "Function strips HTML tags from string."
  [s]
  (.text (soup/parse s)))
