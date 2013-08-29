(defproject flourish-common "0.1.0"
  :description "common utility functions"
  :aot :all
  :dependencies [[cheshire "5.0.2"]
                 [enlive "1.1.1"]
                 [me.raynes/fs "1.4.0"]                                  
                 [org.clojure/tools.reader "0.7.6"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [ring "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.6"]]
  :plugins [[s3-wagon-private "1.1.2"]
            [lein-expectations "0.0.7"]
            [lein-marginalia "0.7.1"]            
            [lein-autoexpect "0.2.5"]]
  :profiles {:dev {:dependencies [[clj-stacktrace "0.2.4"]
                                  [lein-autodoc "0.9.0"]                              
                                  [expectations "1.4.33"]]}}
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/"
                             :username :env
                             :passphrase :env}]])

