(ns megafono_feed.models.channel
  (:require [clojure.java.jdbc :as sql]
            [korma.db :as db]
            [heroku-database-url-to-jdbc.core :as h]))

(use 'korma.db)
(defdb db (h/korma-connection-map (or (System/getenv "DATABASE_URL")
                               "postgresql://127.0.0.1:5432/megafono_development")))

(use 'korma.core)
(defentity channels)

(defn all []
  (into [] (select channels
    (fields :uid :name))))

(defn find_by_slug [slug]
  (first
    (select channels
      (where {:slug slug})
      (limit 1))))
