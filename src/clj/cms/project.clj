(defproject cms "0.1.0"
  :description "Library for working with resources published via bricolage cms"
  :url "http://causalmarketing.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]                 
                 [me.raynes/fs "1.4.0"]
                 [cheshire "5.0.2"]
                 [compojure "1.1.5"]
                 [enlive "1.1.1"]
                 [flourish-common "0.1.0-SNAPSHOT"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [ring-middleware-format "0.3.0"]                 
                 [ring/ring-jetty-adapter "1.1.6"]
                 [ring.middleware.logger "0.4.0"]
                 [ring-mock "0.1.3"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler cms.handler/app}
  :dev-dependencies [[lein-autodoc "0.9.0"]])
