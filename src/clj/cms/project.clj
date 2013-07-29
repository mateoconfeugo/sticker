(defproject cms "0.1.0"
  :description "Library for working with content managemed resourcess and
   other original user authored content"
  :url "http://causalmarketing.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :aot :all
  :dependencies [[cheshire "5.0.2"] ; JSON <-> clojure 
                 [compojure "1.1.5"] ; Web routing https://github.com/weavejester/compojure
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client https://github.com/clojure-clutch/clutch
                 [com.taoensso/timbre "2.2.0"] ; Logging https://github.com/ptaoussanis/timbre
                 [de.ubercode.clostache/clostache "1.3.1"] ; Templationg                 
                 [enlive "1.1.1"] ; DOM manipulating                                   
                 [flourish-common "0.1.0"]  ; Common functionality of the gusto system                 
                 [ibdknox/clojurescript "0.0-1534"] ; ClojureScript compiler https://github.com/clojure/clojurescript
                 [liberator "0.9.0"] ; WebMachine(REST state machine) port to clojure
                 [me.raynes/fs "1.4.0"]  ; File manipulation tools                                 
                 [org.clojure/core.match "0.2.0-rc3"] ; Erlang-esque pattern matching https://github.com/clojure/core.match
                 [org.clojure/clojure "1.5.1"]  ; Lisp on the JVM                                                 
                 [ring-middleware-format "0.3.0"]                 
                 [ring/ring-jetty-adapter "1.1.6"] ; Web Server https://github.com/ring-clojure/ring
                 [ring.middleware.logger "0.4.0"]
                 [ring-mock "0.1.3"]]
  :plugins [[lein-ring "0.8.5"]
            [lein-localrepo "0.4.1"]            
            [s3-wagon-private "1.1.2"]            
            [lein-expectations "0.0.7"]
            [lein-cljsbuild "0.3.2"] ; ClojureScript compiler https://github.com/emezeske/lein-cljsbuild            
            [lein-autoexpect "0.2.5"]]
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/"
                             :username :env
                             :passphrase :env}]]
  :main cms.handler
  :ring {:handler cms.handler/app}
  :profiles  {:dev {:dependencies [[ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.8"]
                                   [clj-webdriver "0.6.0"]
                                   [lein-autodoc "0.9.0"]                              
                                   [expectations "1.4.33"]
                                   [org.clojure/tools.trace "0.7.5"]
                                   [vmfest "0.3.0-alpha.5"]]}})

