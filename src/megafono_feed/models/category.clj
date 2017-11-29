(ns megafono-feed.models.category
  (:require [megafono-feed.models.definitions :refer [categories]]
            [korma.core :as k]))

(defn find-by-id [id]
  (first
    (k/select categories
            (k/where {:id id})
            (k/limit 1))))
