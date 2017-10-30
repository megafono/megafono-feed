(ns megafono_feed.models.channel
  (:require [clojure.java.jdbc :as sql]
            [clj-time.coerce :as c]
            [megafono-feed.models.definitions :refer [
                                                      channels
                                                      episodes
                                                      categories
                                                      channel_ownerships
                                                      users
                                                      db
                                                      slugs]]))

(use 'korma.core)

(def activated_channel (-> (select* channels)
                           (join :inner slugs (and
                                                (= :friendly_id_slugs.sluggable_id :id)
                                                (= :friendly_id_slugs.sluggable_type "Channel")))
                           (with categories)
                           (with episodes
                             (where {:media_status "uploaded"})
                             (where (< :published_at (c/to-sql-time (java.util.Date.))))
                             (order :published_at :DESC))
                           (with channel_ownerships
                             (where {:ownerable_type "User"
                                     :level [in [0 1]]})
                             (limit 1)
                             (with users))
                           (where {:deleted_at nil
                                   :status [not= "pending"]})))

(defn all []
  (into [] (select activated_channel
                   (fields :slug :name))))

(defn find-by-slug [slug]
  (first
    (select activated_channel
            (where {:friendly_id_slugs.slug slug})
            (limit 1))))
