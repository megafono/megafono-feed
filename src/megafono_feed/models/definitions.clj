(ns megafono-feed.models.definitions
  (:require [heroku-database-url-to-jdbc.core :as h]
            [korma.db :refer [
                               defdb
                             ]]
            [korma.core :refer [
                                 defentity
                                 select
                                 table
                                 where
                                 belongs-to
                                 many-to-many
                                 has-many
                                 database
                               ]]
            [black.water.korma :refer [decorate-korma!]]))

(decorate-korma!)

(defdb db (h/korma-connection-map (System/getenv "DATABASE_URL")))

(declare channels episodes users categories subscriptions)

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
  (has-many subscriptions {:fk :channel_id})
  (has-many episodes {:fk :channel_id})
  (has-many channel_ownerships {:fk :channel_id})
  (many-to-many categories :channel_categories {:lfk :channel_id :rfk :category_id})
  (table :channels))

(defentity users
  (database db)
  (table :users))

(defentity categories
  (database db)
  (table :categories)
  (belongs-to categories {:fk :category_id}))

(defentity slugs
  (database db)
  (table :friendly_id_slugs))

(defentity subscriptions
  (database db)
  (table :subscriptions)
  (belongs-to channels {:fk :channel_id}))
