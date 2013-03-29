(defproject causal-marketing "0.1.0-SNAPSHOT"
  :description "Public web presence for Causal Marketing"
  :main causal-marketing.handler
  :aot [ causal-marketing.handler]
  :resource-paths ["resources"]
  :url "http://www.causalmarketing.com"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.6"]                 
                 [enlive "1.1.1"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler causal-marketing.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
