(ns megafono_feed.models.channel
  (:require [clojure.java.jdbc :as sql]
            [korma.db :as db]
            [heroku-database-url-to-jdbc.core :as h]))

(use 'korma.db)
(defdb db (h/korma-connection-map (or (System/getenv "DATABASE_URL")
                               "postgresql://127.0.0.1:5432/megafono_development")))

(use 'korma.core)
(defentity episodes)
(defentity channels
           (has-many episodes))

(def activated_channel (-> (select* channels)
                           (where {:deleted_at nil
                                   :status [not= "pending"]})))

(defn all []
  (into [] (select activated_channel
                   (fields :slug :name))))

(defn find-by-slug [slug]
  (first
    (select activated_channel
            (where {:slug slug})
            (limit 1))))