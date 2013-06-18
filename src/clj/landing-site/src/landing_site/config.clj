(ns landing-site.config
  "Application level objects and settings, you know globals"
  (:use [cms.site :only [new-cms-site]]
        [flourish-common.config]
        [pallet.api :only [ node-spec] :as api]
        [riemann.client :only [send-event tcp-client]]                
        [cheshire.core :only (parse-string)])
  (:require [zookeeper :as zk]))

(defn determine-cfg-location []
  "Obtain the application config file directory"
    (str (System/getProperty "user.dir") "/config"))

;; determine environment variables
;;(def app-user (or (System/getenv  "LSBS_USER") (System/getProperty "user.name")))
(def app-user "pallet-admin")
;;(def app-group (or (System/getenv  "LSBS_GROUP") app-user))
(def app-group "pallet-admin")
(def home-dir (or (System/getenv "LSBS_HOME") (System/getProperty "user.home")))
(def root-dir (or (System/getenv "LSBS_ROOT_DIR") home-dir))
(def website-dir (or (System/getenv "LSBS_WEBSITE") (str root-dir "/website/site")))
;;(def website-dir (or (System/getenv "LSBS_WEBSITE") (str root-dir "/website/patientcomfortreferral.com/site")))
(def cfg-dir (or (System/getenv "LSBS_CFG_DIR") (str root-dir "/website/config")))
;;(def cfg (parse-string (slurp(str cfg-dir "/site-config.json")) true))
(def cfg (parse-string (slurp "config/site-config.json") true))
(def db-address (or (System/getenv "LSBS_DB_ADDRESS") "127.0.0.1"))
(def db-name (or (System/getenv "LSBS_DB_NAME") (str app-user "-lead-db")))
(def db-user (or (System/getenv "LSBS_DB_USER") "root"))
;; TODO: secure this
(def db-password (or (System/getenv "LSBS_DB_PASSWORD") "test123"))
(def static-html-dir (str website-dir "/site/articles/"))
(def zookeeper-uri (or (str (System/getProperty "zookeeper.uri") (-> cfg :zookeeper :uri))))
(def operator-rdn (str (System/getProperty "zookeeper.rdn"(-> cfg :zookeeper :uri))))
(def monitoring-uri (or (System/getenv "LSBS_MONITORING_ADDRESS")  (-> cfg :riemann :host)))
(def monitoring-port (or (System/getenv "LSBS_MONITORING_PORT") (-> cfg :riemann :port)))
(def heatmap-path (or (System/getenv "LSBS_HEATMAP_PATH") (str root-dir "/" (-> cfg :heatmap :log-path))))

;; landing site optimization settings
(def token-type (-> cfg :website :site-cfg-dir-path))
(def token (-> cfg :website :market_vector))
(def site-tag (-> cfg :website :site-tag))
(def site-name (-> cfg :website :site-name))

;; application objects
;;(def cms (new-cms-site {:base-path website-dir :site-cfg-path token-type :site-tag site-tag :site-name site-name}))
(def monitoring-bus (tcp-client :host monitoring-uri :port monitoring-port ))

