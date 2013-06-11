(ns landing-site.groups.delivery
  "Provision and install landing site optimization delivery server"
  (:use [landing-site.dev-ops-config :only [delivery-settings]]        
        [landing-site.servers.delivery :only[delivery-spec]]
        [pallet.api :only [plan-fn node-spec server-spec group-spec]]))

(def delivery-group (group-spec (:group-spec-name delivery-settings)
                                 :node-spec (:node-spec delivery-settings)
                                 :extends [delivery-spec]))                                                        

