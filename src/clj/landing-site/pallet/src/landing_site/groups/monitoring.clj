(ns landing-site.groups.monitoring
  (:use
   [pallet.api :only [group-spec node-spec ]]
        [landing-site [crates.supervise :only [monitoring]
                        config :only [delivery-settings]]]))

(def base-monitoring-group-spec (group-spec (:group-spec-name db-settings)
                                            :node-spec (:node-spec db-settings) :extends [monitoring-server-spec])
