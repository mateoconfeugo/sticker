(ns landing-site.servers.database
  (:use   [pallet.api :only [plan-fn server-spec]]         
          [pallet.actions :only [package set-force-overwrite remote-file remote-directory rsync-directory exec-script*]]
          [pallet.crate :only [defplan]]))

(defplan lsbs-mysql-db-spec
  "install mysql client package and use its tools to create and load the landing site database"
  [{:keys [db-name db-username db-password schema-file provider node-count owner] :as settings}]
  (let [mysql-client-spec (server-spec :phases {:configure (plan-fn (package "mysql-client"))})
        create-landing-database-cmd (str "mysqladmin  -u " db-username  " -p"db-password  " create " db-name)
        load-landing-database-cmd (str "mysql  -u " db-username  " -p"db-password " " db-name " < " "landing-site.sql")]
    (server-spec
     :phases {:configure (plan-fn
                          (remote-file "landing-site.sql" :local-file  schema-file :owner "pallet-admin")
                          (exec-script* create-landing-database-cmd)                                                                                              (exec-script* load-landing-database-cmd))})))