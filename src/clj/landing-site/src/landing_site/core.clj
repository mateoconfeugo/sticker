(ns landing-site.core
  "Application level objects and settings"
  (:use [cms.site :only [new-cms-site]]
        [flourish-common.config]
        [riemann.client :only [send-event tcp-client]]                
        [cheshire.core :only (parse-string)])
  (:require [zookeeper :as zk]))

(defn determine-cfg-location []
  (let [env-set (System/getProperty "LSBS_CFG_DIR")]
    (if env-set
      env-set
      (str (System/getProperty "user.dir") "/website/config"))))

;; environment resource locations
(def app-user (System/getenv  "LSBS_USER"))
(def app-group (System/getenv  "LSBS_GROUP"))
(def website-dir (System/getenv "LSBS_WEBSITE"))
(def home-dir (System/getenv "LSBS_HOME"))
(def root-dir (System/getenv "LSBS_ROOT_DIR"))
(def cfg-dir (System/getenv "LSBS_CFG_DIR"))
(def cfg (parse-string (slurp(str cfg-dir "/site-config.json")) true))
(def db-address (System/getenv "LSBS_DB_ADDRESS"))
(def db-name (System/getenv "LSBS_DB_NAME"))
(def db-user (System/getenv "LSBS_DB_USER"))
(def db-password (System/getenv "LSBS_DB_PASSWORD"))
(def static-html-dir (str website-dir "/site/articles/"))
(def zookeeper-uri (str (System/getProperty "zookeeper.uri")))
(def operator-rdn (str (System/getProperty "zookeeper.rdn")))
(def monitoring-uri (or (System/getenv "LSBS_MONITORING_ADDRESS")  (-> cfg :riemann :host)))
(def monitoring-port (or (System/getenv "LSBS_MONITORING_PORT") (-> cfg :riemann :port)))

;; configuration scheme
(comment
  (def zoo-client (zk/connect (or zookeeper-uri "127.0.0.1:2181")))
  (def cfg (if zoo-client
             (parse-string (String. (:data (zk/data client operator-rdn)) "UTF-8") true)
             (read-config (new-config {:cfg-file-path (str cfg-dir "/site-config.json")}))))
  )

;; settings
(def token-type (-> cfg :website :site-cfg-dir-path))
(def token (-> cfg :website :market_vector))
(def site-tag (-> cfg :website :site-tag))
(def site-name (-> cfg :website :site-name))


;; application objects
(def cms (new-cms-site {:base-path website-dir :site-cfg-path token-type :site-tag site-tag :site-name site-name}))
(def monitoring-bus (tcp-client :host monitoring-uri :port monitoring-port ))
