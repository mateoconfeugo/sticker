(defproject management "0.1.0"
  :description "management gui for the overall system to administer users, accounts and relevant operations
                other business components plugin into it"
  :url "http://marketwithgusto.com/mgnt/about/development.html"
  :license {:name "MIT License"  :url "http://opensource.org/licenses/MIT"}
;;  :hooks [leiningen.cljsbuild]
  :resource-paths ["resources"]
  :source-paths ["src-clj"]
  :min-lein-version "2.0.0"
  :main management.handler
  :ring {:handler management.handler/app :auto-reload? true :auto-refresh true
         :reload-paths ["src-clj" "src-cljs" "resources/templates/user_dashboard.html"]}
  :dependencies [[org.clojure/clojure "1.5.1"] ; Lisp on the JVM
                 [cms "0.1.0"] ; Content management system
                 [flourish-common "0.1.0"] ; Common functionality of the gusto system
                 [site-builder-udc "0.1.0"] ; Online responsive html web site builder
                 [clj-webdriver "0.6.0"]
                 [cheshire "5.2.0"] ; JSON <-> clojure
                 [clj-aws-s3 "0.3.7"] ; Access Amazons S3 bitbuckets
                 [clj-ssh "0.5.6"]
                 [abengoa/clj-stripe "1.0.3"]
;;                 [org.clojars.technomancy/heroku-api "0.1-SNAPSHOT"]
                 [heroku-clj "0.1.0-SNAPSHOT"]
                 [com.heroku.api/heroku-api "0.15"]
                 [com.heroku.api/heroku-json-jackson "0.15"]
                 [com.heroku.api/heroku-http-apache "0.15"]
                 [clojure-mail "0.1.3-SNAPSHOT"]
                 [clojurewerkz/scrypt "1.0.0"] ; Strong encryption and hashing
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client
                 [com.cemerick/friend "0.2.0" :exclusions [xerces/xercesImpl]] ; Role based authentication
                 [com.cemerick/valip "0.3.2"  :exclusions [xerces/xercesImpl]]
                 [com.taoensso/timbre "2.6.2"] ; Logging
                 [com.draines/postal "1.11.0"]
                 [clj-time "0.6.0"]

                 ;;                 [clj-http "0.7.7"]
                 [http-kit "2.1.12"]
                 [compojure "1.1.5"] ; Web routing
                 [enlive "1.1.4"]
                 [domina "1.0.2"]
                 [de.ubercode.clostache/clostache "1.3.1"] ; Templationg
                 [himera "0.1.0-SNAPSHOT"] ; ClojureScript compiler service.
                 [hiccups "0.2.0"] ; HTML authoring library
                 [jayq "2.4.0"]
                 [korma "0.3.0-RC5"] ; ORM
                 [liberator "0.9.0"] ; WebMachine(REST state machine) port to clojure
                 [me.raynes/fs "1.4.5"] ; File manipulation tools
                 [metis "0.3.3"] ; Form validation
                 [mysql/mysql-connector-java "5.1.26"] ; mysql jdbc
                 [org.clojure/java.jdbc "0.2.3"] ; jdbc client
                 [org.apache.httpcomponents/httpclient-osgi "4.3.1"]
                 [org.clojure/google-closure-library-third-party "0.0-2029-2"]
                 [org.clojure/clojurescript "0.0-1934"]
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]
                 [org.clojure/core.match "0.2.0"]
                 [antler/postmark "1.2.0"]
                 [riemann-clojure-client "0.2.6"] ; Monitoring client
                 [ring.middleware.logger "0.4.3"]
                 [org.clojars.pallix/xml-apis "2.5.0"]
                 [ring/ring-jetty-adapter "1.2.0"] ; Web Server
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [shoreleave "0.3.0"]
                 [tentacles "0.2.6"]
                 [zookeeper-clj "0.9.3"]
                 [com.palletops/pallet "0.8.0-RC.3"]
                 [com.palletops/pallet-vmfest "0.3.0-beta.2"]
                 [vmfest "0.3.0-beta.3"]
                 [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]]
  :plugins [[lein-ring "0.8.7"]
            [lein-set-version "0.3.0"]
            [lein-cljsbuild "0.3.3"] ; clojurescript build
            [lein-marginalia "0.7.1"] ; literate programming
            [lein-pprint "1.1.1"]
            [lein-bikeshed "0.1.3"] ; tell you your code is bad, and that you should feel bad.
            [jonase/eastwood "0.0.2"] ; clojure lint
            [lein-kibit "0.0.8"]  ;; static code analyzer
            [s3-wagon-private "1.1.2"] ; deploy to s3 bit bucket on amazon cloud
            [com.palletops/pallet-lein "0.6.0-beta.9"] ; lein depoyment via palletops
            [lein-expectations "0.0.7"] ; run expect test
            [lein-autoexpect "0.2.5"] ; run expect tests when files change
            [configleaf "0.4.6"] ; access this file from the application
            [lein-cloverage "1.0.2"]] ; profiler
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/" :username :env :passphrase :env}]
                 ["sonatype-staging"  {:url "https://oss.sonatype.org/content/groups/staging/"}]]

  :profiles  {:dev {:dependencies [[expectations "1.4.56"]
                                   [org.clojure/tools.trace "0.7.6"]
                                   [ring-mock "0.1.5"]
                                   [ring/ring-devel "1.2.0"]
                                   [vmfest "0.3.0-beta.3"]]}
              :pallet {:dependencies
                       [[com.palletops/pallet "0.8.0-RC.3"]
                        [com.palletops/pallet-vmfest "0.3.0-beta.2"]
                        [vmfest "0.3.0-beta.3"]
                        [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]
                        [com.palletops/java-crate "0.8.0-beta.5"]
                        [com.palletops/runit-crate "0.8.0-alpha.1"]
                        [com.palletops/app-deploy-crate "0.8.0-alpha.3"]
                        [org.cloudhoist/pallet-jclouds "1.5.2"]
                        [org.jclouds/jclouds-all "1.6.0"]
                        [org.jclouds.driver/jclouds-slf4j "1.6.0" :exclusions [org.slf4j/slf4j-api]]
                        [org.jclouds.driver/jclouds-sshj "1.6.0"]
                        [ch.qos.logback/logback-classic "1.0.13"]
                        [org.slf4j/jcl-over-slf4j "1.7.5"]]}}


  :cljsbuild {
              :crossovers [valip.core
                           valip.predicates
                           management.login.validators]
;;              :crossover-path "crossover-cljs"
              :crossover-jar false
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
                        :jar true
                        :compiler {:output-to "resources/public/js/management-debug.js"
                                   :output-dir "out"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :jar true
                        :compiler {:output-to "resources/public/js/management.js"
                                   :optimizations :advanced
                                   :pretty-print false}}
}}
  )

(comment
                       :test
                       {:source-paths ["test-cljs"]
                        :compiler {:output-to "resources/private/js/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
  )
