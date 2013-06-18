(defproject landing-site "0.1.0"
  :description "http landing site optimization server"
  :url "https://www.marketwithgusto.com/dev/landing-site-server"
  :resource-paths ["resources"]
  :dependencies [[aleph "0.3.0-rc1"]
                 [cheshire "5.0.2"]
                 [clj-logging-config "1.9.7"]                 
                 [clj-socketio "0.2.3"]                 
                 [clj-ssh "0.5.6"]                 
                 [cms "0.1.0-SNAPSHOT"]
                 [com.draines/postal "1.10.2"]                                  
                 [com.googlecode.libphonenumber/libphonenumber "5.4"]
                 [com.ibdknox.socket_io_netty/socket-io-netty "0.3.4"]
                 [compojure "1.1.5"]
                 [de.ubercode.clostache/clostache "1.3.1"]                 
                 [flourish-common "0.1.0-SNAPSHOT"]
                 [korma "0.3.0-RC5"]
                 [log4j/log4j "1.2.16" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]                 
                 [metis "0.3.0"]                 
                 [mysql/mysql-connector-java "5.1.6"]                 
                 [org.apache.httpcomponents/httpclient-osgi "4.2.5"]
                 [org.clojure/clojure "1.5.1"]                 
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.slf4j/slf4j-log4j12 "1.6.4"]
                 [com.palletops/pallet "0.8.0-beta.9"]                 
                 [riemann-clojure-client "0.2.1"]                 
                 [ring/ring-jetty-adapter "1.1.6"]
                 [ring.middleware.logger "0.4.0"]
                 [zookeeper-clj "0.9.1"]]                 
  :jvm-opts ["-Dvbox.home=/Applications/VirtualBox.app/Contents/MacOS"]
  :html5-docs-source-path "src/landing_site"
  :plugins [[lein-ring "0.8.5"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "0.2.5"]
            [lein-dpkg "0.1.0"]
;;            [lein-ritz "0.6.0"]            
            [lein-html5-docs "2.0.0"]
            [com.palletops/pallet-lein "0.6.0-beta.9"]
            [lein-cloverage "1.0.2"]]            
  :ring {:handler landing-site.handler/app}
  :main landing-site.handler
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]
                                  [org.clojure/tools.trace "0.7.5"]
                                  [expectations "1.4.33"]
                                  [clj-webdriver "0.6.0"]]}
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
                       [org.jclouds.driver/jclouds-slf4j "1.4.2"
                        :exclusions [org.slf4j/slf4j-api]]
                       [org.jclouds.driver/jclouds-sshj "1.4.2"]
                       [ch.qos.logback/logback-classic "1.0.9"]
                       [mysql-crate "0.1.0-SNAPSHOT"]                       
                       [org.slf4j/jcl-over-slf4j "1.7.3"]]}}
    :repositories {"sonatype-snapshots" "https://oss.sonatype.org/content/repositories/snapshots"
                   "sonatype" "https://oss.sonatype.org/content/repositories/releases/"
                   "local" "/usr/local/localrepo"})


