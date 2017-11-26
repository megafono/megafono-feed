(ns megafono-feed.models.category
  (:require [megafono-feed.models.definitions :refer [categories]]))

(use 'korma.core)

(defn find-by-id [id]
  (first
    (select categories
            (where {:id id})
            (limit 1))))
