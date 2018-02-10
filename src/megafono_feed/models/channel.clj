(ns megafono-feed.models.channel
  (:require [clojure.java.jdbc :as sql]
            [clj-time.coerce :as c]
            [korma.core :as k]
            [megafono-feed.models.definitions :refer [channels
                                                      episodes
                                                      categories
                                                      channel_ownerships
                                                      subscriptions
                                                      users
                                                      db
                                                      slugs]]))

(def activated_channel (-> (k/select* channels)
                           (k/modifier "DISTINCT")
                           (k/join :inner slugs (and
                                                  (= :friendly_id_slugs.sluggable_id :id)
                                                  (= :friendly_id_slugs.sluggable_type "Channel")))
                           (k/where {:deleted_at nil
                                     :status [not= "pending"]})
                           (k/order :name)))

(def activated_channel_with_relashionship (-> (k/select* activated_channel)
                                              (k/with categories)
                                              (k/with episodes
                                                (k/where {:media_status "uploaded"})
                                                (k/where (< :published_at (c/to-sql-time (java.util.Date.))))
                                                (k/order :published_at :DESC))
                                              (k/with channel_ownerships
                                                (k/where {:ownerable_type "User"
                                                          :level [in [0 1]]})
                                                (k/limit 1)
                                                (k/with users))
                                              (k/with subscriptions)))

(defn all []
  (into [] (k/select activated_channel
                   (k/fields :slug :name))))

(defn find-by-slug [slug]
  (first
    (k/select activated_channel_with_relashionship
            (k/where {:friendly_id_slugs.slug slug})
            (k/limit 1))))
