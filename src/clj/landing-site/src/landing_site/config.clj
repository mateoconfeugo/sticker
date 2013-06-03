(ns landing-site.config
  "Application level objects and settings"
  (:use [cms.site :only [new-cms-site]]
        [flourish-common.config]
        [pallet.api :only [ node-spec] :as api]
        [riemann.client :only [send-event tcp-client]]                
        [cheshire.core :only (parse-string)])
  (:require [zookeeper :as zk]))

(defn determine-cfg-location []
  (let [env-set (System/getProperty "LSBS_CFG_DIR")]
    (if env-set
      env-set
      (str (System/getProperty "user.dir") "/website/config"))))

;; determine environment variables
;;(def app-user (or (System/getenv  "LSBS_USER") (System/getProperty "user.name")))
(def app-user "pallet-admin")
;;(def app-group (or (System/getenv  "LSBS_GROUP") app-user))
(def app-group "pallet-admin")
(def home-dir (or (System/getenv "LSBS_HOME") (System/getProperty "user.home")))
(def root-dir (or (System/getenv "LSBS_ROOT_DIR") home-dir))
(def website-dir (or (System/getenv "LSBS_WEBSITE") (str root-dir "/website")))
(def cfg-dir (or (System/getenv "LSBS_CFG_DIR") (str root-dir "/website/config")))
(def cfg (parse-string (slurp(str cfg-dir "/site-config.json")) true))
(def db-address (or (System/getenv "LSBS_DB_ADDRESS") "127.0.0.1"))
(def db-name (or (System/getenv "LSBS_DB_NAME") (str app-user "-lead-db")))
(def db-user (or (System/getenv "LSBS_DB_USER") "root"))
(def db-password (or (System/getenv "LSBS_DB_PASSWORD") "test123"))
(def static-html-dir (str website-dir "/site/articles/"))
(def zookeeper-uri (or (str (System/getProperty "zookeeper.uri") (-> cfg :zookeeper :uri))))
(def operator-rdn (str (System/getProperty "zookeeper.rdn"(-> cfg :zookeeper :uri))))
(def monitoring-uri (or (System/getenv "LSBS_MONITORING_ADDRESS")  (-> cfg :riemann :host)))
(def monitoring-port (or (System/getenv "LSBS_MONITORING_PORT") (-> cfg :riemann :port)))

;; landing site optimization settings
(def token-type (-> cfg :website :site-cfg-dir-path))
(def token (-> cfg :website :market_vector))
(def site-tag (-> cfg :website :site-tag))
(def site-name (-> cfg :website :site-name))

;; application objects
(def cms (new-cms-site {:base-path website-dir :site-cfg-path token-type :site-tag site-tag :site-name site-name}))
(def monitoring-bus (tcp-client :host monitoring-uri :port monitoring-port ))

;; DEV-OPS CONFIGS
;; user admin
(def install-owner "pallet-admin")

;; default node
(def delivery-node-spec (api/node-spec :image {:image-id :java-mysql-postfix}))
;; monitoring

(def monitoring-settings  {:riemann-cmd "nohup ~/bin/riemann ~/etc/riemann.config"
                          :dist-url "http://aphyr.com/riemann/riemann-0.2.1.tar.bz2"
                          :riemann-cfgs-dir  (str root-dir "/etc")
                          :install-dir root-dir
                          :local-cfg-file "resources/riemann.config"
                          :remote-cfg-file "~/etc/riemann.config"
                          :user "riemann"})

;; cms
;; TODO: figure out how to have it compress the local-dir or add some function to make the tarball
(def remote-cms-dir website-dir)
(def local-cms-dir "resources/site.tgz")

;; daemontools
;; TODO: remove the dependency on rsync to transfer directory use the same stuff as cms
(def rsync-from-dir "resources/supervise/landing-site")
(def rsync-to-dir (str root-dir "service/landing-site"))

;; database
(def schema-file "resources/landing-site.sql")
(def lsbs-db-user db-user)
(def lsbs-db-password db-password)
(def lsbs-db-name db-name)
(def db-group-name "lsbs-db")
(def db-settings {:group-spec-name db-group-name :schema-file schema-file :db-username lsbs-db-user 
                  :db-password lsbs-db-password :db-name lsbs-db-name
                  :owner "pallet-admin" :provider :vmfest :node-count 1
                  :node-spec delivery-node-spec})

;; lsbs delivery app
(def remote-root-dir root-dir)
(def remote-cfg-dir cfg-dir)
(def remote-website-dir website-dir)
;;(def remote-jar (str root-dir "landing-site.jar"))
(def remote-jar  "~/landing-site.jar")
(def local-jar  "target/landing-site-0.1.0-standalone.jar")
(def app-launch-cmd  "java -jar /home/pallet-admin/landing-site.jar")
(def delivery-label "lsbs-delivery")
(def delivery-settings {:db-name lsbs-db-name
                        :db-username lsbs-db-user
                        :db-password lsbs-db-password
                        :owner install-owner
                        :group-spec-name delivery-label
                        :local-jar local-jar
                        :remote-jar remote-jar
                        :root-dir remote-root-dir
                        :cfg-dir remote-cfg-dir
                        :website-dir remote-website-dir
                        :rsync-from rsync-from-dir
                        :rsync-to rsync-to-dir
                        :cms-local-dir local-cms-dir
                        :cms-remote-dir remote-cms-dir
                        :dist-url (:dist-url monitoring-settings)
                        :node-spec delivery-node-spec
                        :node-count 1
                        :group app-group
                        :monitoring-cfg-dir (:riemann-cfgs-dir monitoring-settings)
                        :provider (or (System/getenv "LSBS_PALLET_PROVIDER") :vmfest)})


;; configuration scheme
(comment
  (def zoo-client (zk/connect (or zookeeper-uri "127.0.0.1:2181")))
  (def cfg (if zoo-client
             (parse-string (String. (:data (zk/data client operator-rdn)) "UTF-8") true)
             (read-config (new-config {:cfg-file-path (str cfg-dir "/site-config.json")}))))
  )
