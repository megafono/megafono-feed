(ns megafono-feed.views.helpers.time
  (:import java.util.Date
           (java.text SimpleDateFormat)))

(defn format-rss [time]
  (.format (new SimpleDateFormat
                "EEE, dd MMM yyyy HH:mm:ss ZZZZ") time))
