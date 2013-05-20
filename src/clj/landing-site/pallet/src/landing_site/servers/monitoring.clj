(ns landing-site.server.monitoring
  "Returns the group spec to provision, install and load landing site database"
  (:use [clojure.core]
        [pallet.api :only [server-spec]]

(def monitoring-server-spec (server-spec :extends[(-> delivery-settings create-monitoring-spec)])