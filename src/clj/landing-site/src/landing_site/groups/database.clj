(ns landing-site.groups.database
  "Create a node that installs, creates, runs the landing site databases"
  (:use  [landing-site.servers.database]
         [landing-site.dev-ops-config :only[db-settings]]        
         [landing-site.servers.packages :only[with-mysql-client]]
         [pallet.api :only [group-spec converge lift]]
         [pallet.configure :only [compute-service]]))

(def database-group (group-spec (:group-spec-name db-settings)
                                :node-spec (:node-spec db-settings)
                                :extends [with-mysql-client copy-schema create-database load-schema]))


