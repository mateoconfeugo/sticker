(ns landing-site.servers.database
  (:use [landing-site.dev-ops-config :only[db-settings]]
        [landing-site.servers.packages :only[with-mysql-server with-mysql-client]]
        [pallet.api :only[server-spec plan-fn]]
        [pallet.actions :only[remote-file exec-script*]]))

(def copy-schema (server-spec :phases {:configure (plan-fn
                                                   (remote-file "landing-site.sql"
                                                                :local-file (:schema-file db-settings)
                                                                :owner (:owner db-settings)))}))

(def create-cmd (str "mysqladmin  -u " (:db-username db-settings) " -p"(:db-password db-settings) " create " (:db-name db-settings)))

(def drop-cmd (str "mysqladmin -u" (:db-username db-settings) " -p"(:db-password db-settings) " -f drop " (:db-name db-settings)))

(def load-cmd (str "mysql  -u " (:db-username db-settings)  " -p"(:db-password db-settings) " mysql  < landing-site.sql"))

(def create-database (server-spec :phases {:configure (plan-fn
                                                       (exec-script* drop-cmd)
                                                       (exec-script* create-cmd))}))

(def load-schema (server-spec :phases {:configure (plan-fn
                                                   (exec-script* load-cmd))}))

(def lead-database-spec (server-spec :extends [with-mysql-server with-mysql-client copy-schema load-schema]))