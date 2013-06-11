(ns landing-site.groups.monitoring
  "sets up riemann monitoring"
  (:use [landing-site.servers.monitoring :only [monitoring-spec]]
        [landing-site.dev-ops-config :only [monitoring-settings]]]   
  [pallet.api :only [group-spec node-spec ]]))

(def monitoring-group (group-spec (:group-spec-name monitoring-settings)
                                  :node-spec (:node-spec monitoring-settings) :extends [monitoring-spec])
