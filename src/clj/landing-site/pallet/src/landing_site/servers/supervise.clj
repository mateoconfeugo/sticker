(ns landing-site.server.supervise
  "Provision and install landing site optimization server"
  (:use [pallet.api :only [server-spec]]
        [landing-site [crates.supervise :only [setup-superviser]
                        config :only [delivery-settings]]]))

(def supervise-spec (server-spec :extends [(-> delivery-settings setup-supervisor)]))
