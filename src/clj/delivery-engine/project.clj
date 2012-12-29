(defproject delivery-engine "1.0.0-SNAPSHOT"
  :description "delivery engine component that retreives listings when given query along with some other data"
  :aot :all
  :main delivery-engine.search
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.sleepycat/je "4.0.92"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [cheshire "5.0.1"]
                 [cupboard "1.0.0-SNAPSHOT"]                 
                 [clj-http "0.6.2"]
                 [http.async.client "0.5.0"]                 
                 [clj-record "1.1.1"]
                 [enlive "1.0.1"]
                 [ring "1.1.6"]
                 [org.clojars.tavisrudd/redis-clojure "1.3.1"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]                 
                 ])