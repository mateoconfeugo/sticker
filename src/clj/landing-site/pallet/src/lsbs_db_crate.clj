(ns lsbs-db-crate
  (:require
   [pallet.core :only [group-spec node-spec ]]
   [pallet.configure :only [compute-service defpallet]]     
   [pallet.api :only [plan-fn]]
   [pallet.core.session]
   [pallet.crate.environment]
   [pallet.crate :refer [defplan]]
   [pallet.script :as script]
   [pallet.action :only[ with-action-options]]
   [pallet.actions :only [package set-force-overwrite remote-file remote-directory rsync-directory]])
  (:use [clojure.core]))

(def schema-path "/Users/matthewburns/github/florish-online/src/clj/landing-site/landing-site.sql")
(def lsbs-db-user "root")
(def lsbs-db-password "test123")
(def lsbs-db-name "pcs")
(create-lsbs-db {:schema-file schema-path
                 :db-username lsbs-db-user
                 :db-password lsbs-db-password
                 :db-name lsbs-db-name
                 :owner pallet-admin
                 :provider :vmfest
                 :node-count 1})

;;(def db-nodes (pallet.api/converge  {lsbs-database 0} :compute db-provider-host))

(def landing-site-schema (slurp schema-path))

;; Provision and install landing site database
(defplan create-lsbs-mysql-db
  [{:keys [db-name db-username db-password schema-file provider node-count owner] :as settings}]
  (let [load-landing-database-cmd (str "mysql  -u " db-username  " -p"db-password " " db-name " < " "landing-site.sql")
        create-landing-database-cmd (str "mysqladmin  -u " db-username  " -p"db-password  " create " db-name)
        mysql-client-spec (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn ((pallet.actions/package "mysql-client")))})
        load-lsbs-database-spec (pallet.api/server-spec
                         :phases {:configure (pallet.api/plan-fn
                                              (do
                                                (pallet.actions/remote-file "landing-site.sql" :local-file  schema-file :owner "pallet-admin")
                                                (pallet.actions/exec-script* create-landing-database-cmd)                                                
                                                (pallet.actions/exec-script* load-landing-database-cmd)
                                                ))})
        lsbs-database-group-spec (pallet.api/group-spec "lsbs-database" :node-spec delivery-node-spec :extends [load-lsbs-database-spec])
        db-provider-host (pallet.configure/compute-service provider)
        db-nodes (future (pallet.api/converge  {lsbs-database node-count} :compute db-provider-host))]
        db-node  ((first (@db-nodes :targets)) :node)))

