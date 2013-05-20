(ns landing-site.servers.delivery
  "Provision and install landing site optimization delivery server"
  (:use [pallet.api :only [server-spec]]
        [landing-site.config :only[delivery-settings]]
        [landing-site.servers [cms :only[create-cms-lsbs]
                               monitoring :only [create-monitoring-server]]))

(def base-lsbs-server-spec (server-spec (:extends (-> delivery-settings create-cms-lsbs create-monitoring-server
                                                      create-delivery-lsbs-spec))))

