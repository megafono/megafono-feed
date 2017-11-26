(defproject megafono_feed "0.1.0"
  :description "Feed generator app"
  :url "https://feed.megafono.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"],
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-defaults "0.1.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/data.xml "0.2.0-alpha5"]
                 [heroku-database-url-to-jdbc "0.2.2"]
                 [korma "0.4.3"]
                 [clj-time "0.14.0"]
                 [clj-soup/clojure-soup "0.1.3"]
                 [jstrutz/hashids "1.0.1"]
                 [environ "1.0.0"]
                 [blackwater "0.0.9"]]
  :min-lein-version "2.0.0"
  :main ^:skip-aot megafono_feed.core
  :uberjar-name "megafono_feed-standalone.jar"
  :plugins [[lein-ring "0.8.13"]
            [environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :ring {:handler megafono_feed.core/application}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}
             :production {:env {:production true}}
             :uberjar {:aot :all}}
  :jvm-opts ["-Duser.timezone=UTC"])
