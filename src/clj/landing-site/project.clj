(defproject landing-site "0.1.0"
  :description "http landing site server"
  :url "http://www.marketwithgusto.com/products/landing-site-server"
  :resource-paths ["resources"]  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [me.raynes/fs "1.4.0"]
                 [flourish-common "0.1.0-SNAPSHOT"]
                 [cms "0.1.0-SNAPSHOT"]
                 [com.cemerick/friend "0.1.4"]
                 [robert/hooke "1.3.0"]
                 [com.googlecode.libphonenumber/libphonenumber "5.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 [korma "0.3.0-RC5"]                 
                 [metis "0.3.0"]
                 [cheshire "5.0.2"]                 
                 [expectations "1.4.33"]]
  :plugins [[lein-ring "0.8.2"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "0.2.5"]]
  :ring {:handler landing-site.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})

