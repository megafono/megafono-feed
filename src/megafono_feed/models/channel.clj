(ns megafono_feed.models.channel
  (:require [clojure.java.jdbc :as sql]
            [megafono-feed.models.definitions :refer [channels episodes db]]))

(use 'korma.core)

(def activated_channel (-> (select* channels)
                           (where {:deleted_at nil
                                   :status [not= "pending"]})))

(defn all []
  (into [] (select activated_channel
                   (fields :slug :name))))

(defn find-by-slug [slug]
  (first
    (select activated_channel
            (with episodes)
            (where {:slug slug})
            (limit 1))))