(ns megafono-feed.models.definitions
  (:require [heroku-database-url-to-jdbc.core :as h]
            [black.water.korma :refer [decorate-korma!]]))

(decorate-korma!)

(use 'korma.db)
(defdb db (h/korma-connection-map (or (System/getenv "DATABASE_URL")
                                      "postgresql://127.0.0.1:5432/megafono_development")))

(use 'korma.core)

(declare channels episodes)

(defentity episodes
           (database db)
           (table :episodes)
           (belongs-to channels {:fk :episode_id}))

(defentity channels
           (database db)
           (has-many episodes {:fk :channel_id})
           (table :channels))
