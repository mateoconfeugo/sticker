(ns landing-site.groups.monitoring
  (:use
   [pallet.api :only [group-spec node-spec ]]
   [landing-site [servers.monitoring :only [monitoring-spec]
                  config :only [monitoring-settings]]]))

(def monitoring-group (group-spec (:group-spec-name monitoring-settings)
                                  :node-spec (:node-spec monitoring-settings) :extends [monitoring-spec])
