(ns landing-site.servers.cms
  "Copy cms resources from publisher master to delivery "  
  (:use [clojure.core]   
        [pallet.api :only [server-spec]]
        [landing-site [crates.cms :only [create-cms-lsbs]
                        config :only [delivery-settings]]]))

(def cms-spec (server-spec :extends [(-> delivery-settings create-cms-lsbs)]))