(ns landing-site.groups.database
  (:use
   [pallet.api :only [group-spec node-spec ]]
   [landing-site.handler]
   [landing-site.servers.database]))

(def base-db-group-spec (group-spec (:group-spec-name db-settings)
                                   :node-spec (:node-spec db-settings) :extends [(lsbs-mysql-db-spec db-settings)])

