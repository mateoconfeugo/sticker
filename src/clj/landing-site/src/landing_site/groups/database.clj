(ns landing-site.groups.database
  "Create a node that installs, creates, runs the landing site databases"
  (:use  [landing-site.dev-ops-config :only[qa-release-target]]
         [landing-site.servers.database :only[lead-database-spec]]
         [pallet.api :only [group-spec converge lift]]))

(def db-group-spec (group-spec "flourish-ls" :extends [lead-database-spec]))
(def qa-lead-db (lift db-group-spec :compute qa-release-target))
;;(def production-lead-db (lift db-group-spec :compute production-blue))
;;(def qa-lead-db (lift (group-spec "flourish-ls" :extends [lead-database-spec]) :compute qa-release-target))
;;(def production-lead-db (lift (group-spec "flourish-ls" :extends [lead-database-spec]) :compute production-blue))
  


