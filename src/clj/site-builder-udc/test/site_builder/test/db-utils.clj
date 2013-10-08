(ns test.db-utils
  (:require [flourish-common.db :refer [initialize-management-database drop-database create-database]]))            

(defn setup-test-mgmt-db
  [{:keys [db-name db-port db-host db-user db-password] :as opts}]
  "Create and populate a test management database"
  (let [name (or db-name "test_mgmt")
        port (or db-port 3306)
        db-host (or db-host "127.0.0.1")
        db-user (or db-user "root")
        db-password (or db-password "test123")
        db-spec   {:classname "com.mysql.jdbc.Driver"
                   :subprotocol "mysql"
                   :subname (str "//" db-host ":" db-port "/" name)
                   :user db-user
                   :password db-password}
        _ (create-database db-spec name)
        _ (initialize-management-database db-spec)]
    db-spec))

