(ns management.groups.database
  ^{:author "Matthew Burns"
    :doc  "Create a node that installs, creates, runs the landing site databases"}
  (:require  [management.dev-ops-config :refer [qa-release-target]]
             [management.servers.database :refer [management-database-spec]]
             [pallet.api :refer [group-spec converge lift]]))

(def db-group-spec (group-spec "flourish-mgmt" :extends [management-database-spec]))
(def qa-mgmt-db (lift db-group-spec :compute qa-release-target))
