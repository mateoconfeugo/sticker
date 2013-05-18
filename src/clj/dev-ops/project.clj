(defproject dev-ops "0.1.0-SNAPSHOT"
  :description "Example of deploying a web app with pallet"  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [compojure "1.1.3"]]
  :ring {:handler dev-ops.core/routes}
  :plugins [[lein-ring "0.8.5"]
            [com.palletops/pallet-lein "0.6.0-beta.9"]]
  :main dev-ops.core
  :profiles {:pallet {:dependencies
                      [[com.palletops/pallet "0.8.0-beta.9"]
                       [com.palletops/pallet-vmfest "0.3.0-alpha.4"]
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
                       [org.slf4j/jcl-over-slf4j "1.7.3"]]}
             :exclusions [commons-logging]}
  :repositories
  {"sonatype-snapshots" "https://oss.sonatype.org/content/repositories/snapshots"
   "sonatype" "https://oss.sonatype.org/content/repositories/releases/"})

