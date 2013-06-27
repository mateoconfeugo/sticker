(ns landing-site.dev-ops-config
    "DEV-OPS settings that deterrmine howto set up the landing site server and its dependencies
   in the appropriate manner for the deployment environment"
    (:use [landing-site.config]
          [landing-site.dev-ops :only [pallet-release-target] :as release]
          [pallet.configure :only [compute-service defpallet]]
          [pallet.api :only[node-spec]]))

(def install-owner "pallet-admin")

;; default node
(def delivery-node-spec (node-spec :image {:image-id :java-mysql-postfix}))

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
(def rsync-from-dir  "resources/run")
(def rsync-to-dir (str root-dir "/supervise/landing-site"))

;; database
(def schema-file "resources/landing-site.sql")
(def lsbs-db-user db-user)
(def lsbs-db-password db-password)
(def lsbs-db-name db-name)
(def db-group-name "lsbs-db")
(def db-settings {:group-spec-name db-group-name
                  :schema-file schema-file
                  :db-username lsbs-db-user 
                  :db-password lsbs-db-password
                  :db-name lsbs-db-name
                  :owner "pallet-admin"
                  :provider (or (System/getenv "LSBS_PALLET_PROVIDER") :vmfest)
                  :node-count 1
                  :node-spec delivery-node-spec})

;; lsbs delivery app
(def remote-root-dir root-dir)
(def remote-cfg-dir cfg-dir)
(def remote-website-dir website-dir)
;;(def remote-jar (str root-dir "landing-site.jar"))
(def remote-jar  "landing-site.jar")
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
                        :bin-dir (str remote-root-dir "/bin")
                        :cfg-dir remote-cfg-dir
                        :website-dir remote-website-dir
                        :rsync-from rsync-from-dir
                        :rsync-to rsync-to-dir
                        :cms-site-tgz-url "http://ops1.causalmarketing.com:8081/cms-sites/site.tgz"
                        :cms-local-dir local-cms-dir
                        :cms-remote-dir root-dir
                        :dist-url (:dist-url monitoring-settings)
                        :node-spec delivery-node-spec
                        :node-count 1
                        :group app-group
                        :monitoring-cfg-dir (:riemann-cfgs-dir monitoring-settings)
                        :provider (or (System/getenv "LSBS_PALLET_PROVIDER") :qa-cloud)})

;; Release targets


;; configuration scheme
(comment
  (def zoo-client (zk/connect (or zookeeper-uri "127.0.0.1:2181")))
  (def cfg (if zoo-client
             (parse-string (String. (:data (zk/data client operator-rdn)) "UTF-8") true)
             (read-config (new-config {:cfg-file-path (str cfg-dir "/site-config.json")}))))
  )


(def qa-pallet (defpallet
                                :admin-user  {:username "pallet-admin"
                                 :private-key-path "~/.ssh/id_rsa"
                                 :public-key-path "~/.ssh/id_rsa.pub"}
                   :services  {
                               :data-center {:provider "node-list"
                                             :environment
                                             {:user {:username "pallet-admin"
                                                     :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                                     :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                                             :node-list [["web100" "flourish-ls" "166.78.154.230" :debian]]}}))
  
(def qa-release-target (pallet.configure/compute-service-from-config qa-pallet :data-center {}))


(def cms-pallet (defpallet
                                :admin-user  {:username "pallet-admin"
                                 :private-key-path "~/.ssh/id_rsa"
                                 :public-key-path "~/.ssh/id_rsa.pub"}
                   :services  {
                               :data-center {:provider "node-list"
                                             :environment
                                             {:user {:username "pallet-admin"
                                                     :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                                     :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                                             :node-list [["ops1" "flourish-ls" "166.78.153.58" :debian]]}}))
  
(def production-cms (pallet.configure/compute-service-from-config cms-pallet :data-center {}))

(def qa-delivery-settings {:db-host "127.0.0.1"
                           :username "pcs"
                           :db-name  "pcs"
                           :db-username "pcs"
                           :db-password "test123"
                           :owner "pcs"
                           :group "pcs"
                           :provider :qa-cloud
                           :root-dir "/home/pcs"
                           :cfg-dir "/home/pcs/config"
                           :website-dir "/home/pcs/website"
                           :monitor-host ""})
