(ns megafono-feed.models.definitions
  (:require [heroku-database-url-to-jdbc.core :as h]
            [black.water.korma :refer [decorate-korma!]]))

(decorate-korma!)

(use 'korma.db)
(defdb db (h/korma-connection-map (or (System/getenv "DATABASE_URL")
                                      "postgresql://127.0.0.1:5432/megafono_development")))

(use 'korma.core)

(declare channels episodes users categories)

(defentity channel_ownerships
  (database db)
  (table :channel_ownerships)
  (where (:ownerable_type "User"))
  (belongs-to users {:fk :ownerable_id}))

(defentity episodes
  (database db)
  (table :episodes)
  (belongs-to channels {:fk :episode_id}))

(defentity channels
  (database db)
  (has-many episodes {:fk :channel_id})
  (has-many channel_ownerships {:fk :channel_id})
  (many-to-many categories :channel_categories {:lfk :channel_id :rfk :category_id})
  (table :channels))

(defentity users
  (database db)
  (table :users))

(defentity categories
  (database db)
  (table :categories))
