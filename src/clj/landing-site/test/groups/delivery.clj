(ns groups.delivery
  (:use [expectations]
        [landing-site.config :only[db-settings delivery-settings]]
        [landing-site.core :only[ping]]      
        [landing-site.groups.delivery]
        [pallet.api :only [group-spec converge lift]]
        [pallet.configure :only [compute-service]]))

(def service (pallet.configure/compute-service (:provider db-settings)))
(def delivery-nodes (converge {delivery-group 1} :compute service)
(def delivery-node  ((first (@delivery-nodes :targets)) :node))
(def delivery-ip (.primary-ip delivery-node))
(expect true (ping delivery-ip))
