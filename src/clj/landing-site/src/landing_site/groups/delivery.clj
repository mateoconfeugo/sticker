(ns landing-site.groups.delivery
  "Provision and install landing site optimization delivery server"
  (:use [pallet.api :only [plan-fn node-spec server-spec group-spec]]
        [landing-site.servers.delivery :only[delivery-spec]]
        [landing-site.config :only [delivery-settings]]))

(def delivery-group (group-spec (:group-spec-name delivery-settings)
                                 :node-spec (:node-spec delivery-settings)
                                 :extends [delivery-spec]))                                                        

