(ns landing-site.servers.cms
  "Copy cms resources from publisher master to delivery"  
  (:use [clojure.core]
        [landing-site.crates.cms :only [create-cms-lsbs]]
        [landing-site.dev-ops-config :only [delivery-settings]]        
        [pallet.api :only [server-spec]]))

(def cms-spec (server-spec :extends [(create-cms-lsbs delivery-settings)]))