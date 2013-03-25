(defproject management "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [me.raynes/fs "1.4.0"]
                 [flourish-common "0.1.0-SNAPSHOT"]
                 [com.cemerick/friend "0.1.4"]
                 ]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler management.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
