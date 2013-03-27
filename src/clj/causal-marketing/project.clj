(defproject causal-marketing "0.1.0-SNAPSHOT"
  :description "Public web presence for Causal Marketing"
  :url "http://www.causalmarketing.com"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler causal-marketing.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
