(defproject site-builder-udc "0.1.0"
  :description "The user layer  distributed component of the overall site builder component"
  :min-lein-version "2.0.0"
  :hooks [leiningen.cljsbuild]
  :source-paths ["src-clj"]
  :ring {:handler site-builder-udc.handler/war-handler
         :init site-builder-udc.handler/init
         :destroy site-builder-udc.handler/destroy}
  :main site-builder-udc.server
  :dependencies [[amalloy/ring-gzip-middleware "0.1.2" :exclusions [org.clojure/clojure]]
                 [crypto-random "1.1.0"]
                 [com.cemerick/piggieback "0.1.0"]
                 [compojure "1.1.5"] ; Web routing https://github.com/weavejester/compojure
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client https://github.com/clojure-clutch/clutch
                 [com.taoensso/timbre "2.6.2"] ; Logging
                 [domina "1.0.1"]
                 [org.clojure/clojurescript "0.0-1853"]
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]
                 [org.clojure/core.match "0.2.0"]
                 [enlive "1.1.1"] ; DOM manipulating
                 [enfocus "2.0.0-SNAPSHOT"] ; client side enlive
                 [flourish-common "0.1.0"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [lib-noir "0.4.7" :exclusions [[org.clojure/clojure] [compojure] [hiccup] [ring]]]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]
                 [ring "1.2.0"]
                 [ring-anti-forgery "0.2.1"]
                 [ring-server "0.2.8" :exclusions [[org.clojure/clojure] [ring]]]
                 [ring-refresh "0.1.2" :exclusions [[org.clojure/clojure] [compojure]]]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [shoreleave "0.3.0"]]
  :profiles  {:dev {:dependencies [[ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.8"]
                                   [clj-webdriver "0.6.0"]
                                   [lein-autodoc "0.9.0"]
                                   [expectations "1.4.33"]
                                   [org.clojure/tools.trace "0.7.5"]
                                   [vmfest "0.3.0-alpha.5"]]}}
  :plugins [[lein-cljsbuild "0.3.3"]
            [lein-marginalia "0.7.1"]
            [lein-ring "0.8.5"]
            [lein-localrepo "0.4.1"]
            [s3-wagon-private "1.1.2"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "0.2.5"]]
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/" :username :env :passphrase :env}]
                 ["sonatype-staging"  {:url "https://oss.sonatype.org/content/groups/staging/"}]]
;;  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
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
              :builds {
                       :dev
                       {:source-paths ["src-cljs"]
                        ;;                        :externs ["public/js/layout_manager.js"]
                        :jar true
                        :compiler {:output-to "resources/public/js/main-debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        ;;                        :externs ["public/js/layout_manager.js"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :source-map "main.js.map"}}
                       :test
                       {:source-paths ["test-cljs"]
                        :compiler {:output-to "resources/private/js/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}}})
