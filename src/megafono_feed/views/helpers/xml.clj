(ns megafono-feed.views.helpers.xml
  (:require
    [jsoup.soup :as soup]
    [megafono-feed.models.category :as categories]))

(defmacro tag [id attrs & content]
  `{:tag ~id :attrs ~attrs :content [~@content]})

(defn category-tag [category]
  (tag :itunes:category {:text (:name category)}))

(defmulti build-category-tag (fn[[k _]] (nil? k)))

(defmethod build-category-tag true [[_ categories]]
  (map category-tag categories))

(defmethod build-category-tag false [[parent_id categories]]
  (let [parent (categories/find-by-id parent_id)]
  [(update-in
     (category-tag parent)
     [:content]
     into (map category-tag categories))]))

(defn categories-tag [categories]
  (mapcat build-category-tag (group-by :category_id categories)))

(defn strip-html
  "Function strips HTML tags from string."
  [s]
  (.text (soup/parse s)))
