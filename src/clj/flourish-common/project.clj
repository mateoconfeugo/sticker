(defproject flourish-common "0.1.0"
  :description "common utility functions"
  :aot :all
  :source-paths ["src-clj"]
  :min-lein-version "2.0.0"
  :hooks [leiningen.cljsbuild]  
  :dependencies [[cheshire "5.0.2"]
                 [enlive "1.1.1"]
                 [jayq "2.4.0"]
                 [enfocus "2.0.0-beta1"] ; client side enlive                 
                 [me.raynes/fs "1.4.0"]                                  
                 [org.clojure/tools.reader "0.7.6"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1853"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [org.clojure/core.match "0.2.0-rc5"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [ring "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [domina "1.0.1"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]                 
                 [amalloy/ring-gzip-middleware "0.1.2" :exclusions [org.clojure/clojure]]                 
                 [com.cemerick/piggieback "0.1.0"]                                  
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [shoreleave "0.3.0"]]
  :plugins [[lein-cljsbuild "0.3.2"]
            [s3-wagon-private "1.1.2"]
            [lein-expectations "0.0.7"]
            [lein-marginalia "0.7.1"]            
            [lein-autoexpect "0.2.5"]]
  :profiles {:dev {:dependencies [[clj-stacktrace "0.2.4"]
                                  [lein-autodoc "0.9.0"]                              
                                  [expectations "1.4.33"]]}}
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/" :username :env  :passphrase :env}]
                 ["sonatype-staging"  {:url "https://oss.sonatype.org/content/groups/staging/"}]]
  :cljsbuild {
              :repl-listen-port 9000
              :repl-launch-commands
                                        ;$ lein trampoline cljsbuild repl-launch firefox <URL>
              {"firefox" ["/Applications/Firefox.app/Contents/MacOS/firefox-bin" :stdout ".repl-firefox-out" :stderr ".repl-firefox-err"]
                                        ;$ lein trampoline cljsbuild repl-launch firefox-naked
               "firefox-naked" ["firefox" "resources/private/html/naked.html"
                                :stdout ".repl-firefox-naked-out" :stderr ".repl-firefox-naked-err"]
                                        ;$ lein trampoline cljsbuild repl-launch phantom <URL>
               "phantom" ["phantomjs" "phantom/repl.js" :stdout ".repl-phantom-out" :stderr ".repl-phantom-err"]
                                        ;$ lein trampoline cljsbuild repl-launch phantom-naked
               "phantom-naked" ["phantomjs" "phantom/repl.js" "resources/private/html/naked.html"
                                :stdout ".repl-phantom-naked-out"  :stderr ".repl-phantom-naked-err"]}
              :test-commands  ;$ lein cljsbuild test
              {"unit" ["phantomjs" "phantom/unit-test.js" "resources/private/html/unit-test.html"]}
              :crossovers [flourish-common.crossover]
              :crossover-path "src-cljs/crossover-cljs"
              :crossover-jar true
              :builds {

                       :dev
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/flourish_common_debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :jar true
                        :compiler {:output-to "resources/public/js/flourish_common.js"
                                   :optimizations :advanced
                                   :pretty-print false}}
                       :test
                       {:source-paths ["test-cljs"]
                        :compiler {:output-to "resources/private/js/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}}})


