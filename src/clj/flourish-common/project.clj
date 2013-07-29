(defproject flourish-common "0.1.0"
  :description "common utility functions"
  :aot :all
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [cheshire "5.0.2"]
                 [enlive "1.1.1"]
                 [ring "1.1.8"]
                 [me.raynes/fs "1.4.0"]                 
                 [ring/ring-jetty-adapter "1.1.6"]]
  :plugins [[s3-wagon-private "1.1.2"]]
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/"
                             :username :env
                             :passphrase :env}]])

