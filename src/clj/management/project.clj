(defproject management "0.1.0"
  :description "management gui for the overall system to administer users, accounts and relevant operations
                other business components plugin into it"
  :url "http://marketwithgusto.com/mgnt/about/development.html"
  :license {:name "MIT License"  :url "http://opensource.org/licenses/MIT"}
  :hooks [leiningen.cljsbuild]  
  :resource-paths ["resources"]
  :source-paths ["src-clj"]
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.0.2"] ; JSON <-> clojure 
                 [clj-aws-s3 "0.3.6"] ; Access Amazons S3 bitbuckets                 
                 [clojurewerkz/scrypt "1.0.0"] ; Strong encryption and hashing
                 [cms "0.1.0"] ; Sontent management system
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client 
                 [com.cemerick/friend "0.1.4"] ; Role based authentication                
                 [com.taoensso/timbre "2.2.0"] ; Logging 
                 [compojure "1.1.5"] ; Web routing 
                 [enlive "1.1.1"] ; DOM manipulating                  
                 [de.ubercode.clostache/clostache "1.3.1"] ; Templationg
                 [flourish-common "0.1.0"] ; Common functionality of the gusto system
                 [himera "0.1.0-SNAPSHOT"] ; ClojureScript compiler service.                
                 [ibdknox/clojurescript "0.0-1534"] ; ClojureScript compiler 
                 [korma "0.3.0-RC5"] ; ORM                                
                 [liberator "0.9.0"] ; WebMachine(REST state machine) port to clojure
                 [me.raynes/fs "1.4.1"] ; File manipulation tools                
                 [metis "0.3.0"] ; Form validation                
                 [mysql/mysql-connector-java "5.1.6"] ; mysql jdbc                 
                 [org.apache.httpcomponents/httpclient-osgi "4.2.5"]
                 [org.clojure/clojure "1.5.1"] ; Lisp on the JVM
                 [org.clojure/clojurescript "0.0-1853"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [org.clojure/core.match "0.2.0-rc5"]
                 [org.clojure/java.jdbc "0.2.3"] ; jdbc client
                 [org.clojure/core.match "0.2.0-rc3"] ; Erlang-esque pattern matching 
                 [riemann-clojure-client "0.2.1"] ; Monitoring client
                 [ring.middleware.logger "0.4.0"]                 
                 [ring/ring-jetty-adapter "1.2.0"] ; Web Server
                 [site-builder-udc "0.1.0"] ; Online responsive html web site builder
                 [zookeeper-clj "0.9.1"]] ; Zookeeper configuration client                
  :plugins [[lein-ring "0.8.2"]
            [lein-cljsbuild "0.3.2"] ; clojurescript build            
            [lein-marginalia "0.7.1"] ; literate programming             
            [lein-pprint "1.1.1"]
            [s3-wagon-private "1.1.2"] ; deploy to s3 bit bucket on amazon cloud
            [com.palletops/pallet-lein "0.6.0-beta.9"] ; lein depoyment via palletops
            [lein-expectations "0.0.7"] ; run expect test
            [lein-autoexpect "0.2.5"] ; run expect tests when files change
            [configleaf "0.4.6"] ; access this file from the application
            [lein-cloverage "1.0.2"]] ; profiler
  :ring {:handler management.handler/app}
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/" :username :env :passphrase :env}]
                 ["sonatype-staging"  {:url "https://oss.sonatype.org/content/groups/staging/"}]]
  :main management.handler
  :profiles  {:dev {:dependencies [[ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.8"]
                                   [clj-webdriver "0.6.0"]                 
                                   [expectations "1.4.33"]
                                   [org.clojure/tools.trace "0.7.5"]
                                   [vmfest "0.3.0-alpha.5"]]}                                                    
              :pallet {:dependencies
                       [[com.palletops/pallet "0.8.0-beta.9"]
                        [com.palletops/pallet-vmfest "0.3.0-alpha.4"]
                        [vmfest "0.3.0-alpha.5"]
                        [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]
                        [com.palletops/java-crate "0.8.0-beta.4"]
                        [com.palletops/runit-crate "0.8.0-alpha.1"]
                        [com.palletops/app-deploy-crate "0.8.0-alpha.1"]
                        [org.cloudhoist/pallet-jclouds "1.5.2"]
                        [org.jclouds/jclouds-all "1.5.5"]
                        [org.jclouds.driver/jclouds-slf4j "1.4.2" :exclusions [org.slf4j/slf4j-api]]
                        [org.jclouds.driver/jclouds-sshj "1.4.2"]
                        [ch.qos.logback/logback-classic "1.0.9"]
                        [org.slf4j/jcl-over-slf4j "1.7.3"]]}}
  :mailing-list {:name "management gui dev mailing list"
                 :archive "http://marketwithgusto.com/mgmt-gui-mailing-list-archives"
                 :other-archives ["http://marketwithgusto.com/mgmt-gui-list-archive2"
                                  "http://marketwithgusto.com/mgmt-gui-list-archive3"]
                 :post "list@marketwithgusto.com"
                 :subscribe "list-subscribe@marketwithgusto.com"
                 :unsubscribe "list-unsubscribe@marketwithgusto.com"}
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
                       :test
                       {:source-paths ["test-cljs"]
                        :compiler {:output-to "resources/private/js/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}}}  
  )
