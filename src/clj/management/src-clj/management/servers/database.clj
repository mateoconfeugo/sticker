(ns management.servers.database
  ^{:author "Matthew Burns"
    :doc  "Create a new management database on a the remote machine and load schema"}
  (:require [management.dev-ops-config :refer [db-settings]]
            [management.servers.packages :refer [with-mysql-server with-mysql-client]]
            [pallet.api :refer [server-spec plan-fn]]
            [pallet.actions :refer [remote-file exec-script*]]))

(def copy-schema
  (server-spec :phases {:configure (plan-fn (remote-file "mgmt.sql" :local-file (:schema-file db-settings) :owner (:owner db-settings)))}))

(def create-cmd (format "mysqladmin  -u %s -p%s create %s" (:db-username db-settings) (:db-password db-settings) (:db-name db-settings)))

(def drop-cmd (format  "mysqladmin -u %s -p%s -f drop %s" (:db-username db-settings) (:db-password db-settings) (:db-name db-settings)))

(def create-database (server-spec :phases {:configure (plan-fn (exec-script* drop-cmd) (exec-script* create-cmd))}))

(def load-cmd (format "mysql  -u %s -p%s mysql < %s" (:db-username db-settings) (:db-password db-settings) (:db-schema-path db-settings)))

(def load-schema (server-spec :phases {:configure (plan-fn (exec-script* load-cmd))}))

(def management-database-spec (server-spec :extends [with-mysql-server with-mysql-client copy-schema load-schema]))
