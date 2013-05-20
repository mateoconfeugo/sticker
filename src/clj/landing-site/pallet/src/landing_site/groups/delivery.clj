(ns landing-site.groups.delivery
  "Provision and install landing site optimization delivery server"
  (:use
   [pallet.api :only [plan-fn node-spec server-spec group-spec]]
   [lsbs-config]
   [landing-site.servers.delivery]
   [landing-site.config]]

(def base-delivery-group-spec (group-spec (:group-spec-name delivery-settings)
                                 :node-spec (:node-spec dettings-settings)
                                 :extends [base-server-spec]))                                                        

