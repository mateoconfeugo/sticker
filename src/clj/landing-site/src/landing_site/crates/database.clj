(ns landing-site.crates.database
  (:use   [pallet.api :only [plan-fn server-spec]]         
          [pallet.actions :only [package remote-file exec-script*]]
          [pallet.crate :only [defplan]]))

(defplan create-database-spec
  "install mysql client package and use its tools to create and load the landing site database"
  [{:keys [db-name db-username db-password schema-file provider node-count owner] :as settings}]
  (let [create-cmd (str "mysqladmin  -u " db-username " -p"db-password " create " db-name)
        load-cmd (str "mysql  -u " db-username " -p"db-password  " " db-name " < landing-site.sql")]
    (server-spec
     :phases {:configure (plan-fn
                          (package "mysql-client")                          
                          (remote-file "landing-site.sql" :local-file  schema-file :owner owner)
                          (exec-script* create-cmd)
                          (exec-script* load-cmd))})))