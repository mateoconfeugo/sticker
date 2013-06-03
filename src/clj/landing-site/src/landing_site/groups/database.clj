(ns landing-site.groups.database
  (:use [pallet.api :only [group-spec converge lift]]
        [pallet.configure :only [compute-service]]        
        [landing-site.servers.database]
        [landing-site.config :only[db-settings]]
        [landing-site.servers.packages :only[with-mysql-client]]))

(def database-group (group-spec (:group-spec-name db-settings)
                                :node-spec (:node-spec db-settings)
                                :extends [with-mysql-client copy-schema create-database load-schema]))


