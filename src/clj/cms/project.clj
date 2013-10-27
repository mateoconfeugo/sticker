(defproject cms "0.1.0"
  :description "Library for working with content managemed resourcess and
   other original user authored content"
  :url "http://causalmarketing.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :aot :all
  :dependencies [[cheshire "5.2.0"] ; JSON <-> clojure 
                 [compojure "1.1.5"] ; Web routing https://github.com/weavejester/compojure
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client https://github.com/clojure-clutch/clutch
                 [com.taoensso/timbre "2.6.2"] ; Logging https://github.com/ptaoussanis/timbre
                 [de.ubercode.clostache/clostache "1.3.1"] ; Templationg                 
                 [enlive "1.1.4"] ; DOM manipulating                                   
                 [flourish-common "0.1.0"]  ; Common functionality of the gusto system                 
                 [ibdknox/clojurescript "0.0-1534"] ; ClojureScript compiler https://github.com/clojure/clojurescript
                 [liberator "0.9.0"] ; WebMachine(REST state machine) port to clojure
                 [me.raynes/fs "1.4.5"]  ; File manipulation tools                                 
                 [org.clojure/core.match "0.2.0"] ; Erlang-esque pattern matching https://github.com/clojure/core.match
                 [org.clojure/clojure "1.5.1"]  ; Lisp on the JVM                                                 
                 [ring-middleware-format "0.3.1"]                 
                 [ring/ring-jetty-adapter "1.2.0"] ; Web Server https://github.com/ring-clojure/ring
                 [ring.middleware.logger "0.4.3"]
                 [ring-mock "0.1.5"]]
  :plugins [[lein-ring "0.8.5"]
            [lein-localrepo "0.4.1"]            
            [s3-wagon-private "1.1.2"]            
            [lein-expectations "0.0.7"]
            [lein-marginalia "0.7.1"]
            [lein-cljsbuild "0.3.2"] ; ClojureScript compiler https://github.com/emezeske/lein-cljsbuild            
            [lein-autoexpect "0.2.5"]]
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/"
                             :username :env
                             :passphrase :env}]]
  :main cms.handler
  :ring {:handler cms.handler/app}
  :profiles  {:dev {:dependencies [[ring-mock "0.1.5"]
                                   [ring/ring-devel "1.2.0"]
                                   [clj-webdriver "0.6.0"]
                                   [lein-autodoc "0.9.0"]                              
                                   [expectations "1.4.56"]
                                   [org.clojure/tools.trace "0.7.6"]
                                   [vmfest "0.3.0-beta.3"]]}}
  :cljsbuild {
              :repl-listen-port 9000
              :repl-launch-commands
              {"firefox" ["firefox"]
               "firefox-naked" ["firefox" "resources/public/html/naked.html"]
               "phantom" ["phantomjs" "phantom/page-repl.js"]
               "phantom-naked" ["phantomjs"
                                "phantom/page-repl.js"
                                "resources/public/html/naked.html"
                                :stdout ".repl-phantom-naked-out"
                                :stderr ".repl-phantom-naked-err"]}
              :test-commands
              {"unit" ["phantomjs" "phantom/unit-test.js" "resources/private/html/unit-test.html"]}
              :crossovers [example.crossover]
              :crossover-path "target/my-crossovers"
              :crossover-jar true
              :builds {:main {:source-paths ["src-cljs"]
                              :jar true
                              :notify-command ["growlnotify" "-m"]
                              :incremental true
                              :assert true
                              :compiler {:output-to "resources/public/js/main.js"
                                         :warnings true
                                         :optimizations :whitespace
                                         :pretty-print true
                                         :print-input-delimiter false
                                         :output-dir "target/my-compiler-output-"
                                         :output-wrapper false
                                         :externs ["jquery-externs.js"]
                                         :libs ["closure/library/third_party/closure"]
                                         :foreign-libs [{:file "http://.com/remote.js"
                                                         :provides  ["my.example"]}]}}}})


