(ns landing-site.servers.database
  (:use [landing-site.dev-ops-config :only[db-settings]]
        [landing-site.servers.packages :only[with-mysql-client]]
        [pallet.api :only[server-spec plan-fn]]
        [pallet.actions :only[remote-file exec-script*]]))

(def copy-schema (server-spec :phases {:configure (plan-fn
                                                   (remote-file "landing-site.sql"
                                                                :local-file (:schema-file db-settings)
                                                                :owner (:owner db-settings)))}))

(def create-cmd (str "mysqladmin  -u " (:db-username db-settings) " -p"(:db-password db-settings) " create " (:db-name db-settings)))

(def load-cmd (str "mysql  -u " (:db-username db-settings)  " -p"(:db-password db-settings) " " (:db-name db-settings) " < landing-site.sql"))

(def create-database (server-spec :phases {:configure (plan-fn (exec-script* create-cmd))}))

(def load-schema (server-spec :phases {:configure (plan-fn (exec-script* load-cmd))}))

(def database-spec (server-spec :extends [with-mysql-client copy-schema create-database load-schema]))