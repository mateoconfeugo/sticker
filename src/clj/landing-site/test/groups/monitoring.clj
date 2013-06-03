(ns groups.monitoring
  (:use [expectations]
        [landing-site.groups.monitoring]
        [landing-site.core :only[ping]]
        [landing-site.config :only[db-settings monitoring-settings]]
        [pallet.api :only [group-spec converge lift]]
        [pallet.configure :only [compute-service]]))

(def service (pallet.configure/compute-service (:provider db-settings)))
(def monitoring-nodes (converge {monitoring-group 1} :compute service)
(def monitoring-node  ((first (@monitoring-nodes :targets)) :node))
(def monitoring-ip (.primary-ip monitoring-node))
(expect true (ping monitoring-ip))