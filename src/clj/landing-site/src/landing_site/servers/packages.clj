(ns landing-site.servers.packages
  "packages needed by the different servers"
  (:use [landing-site.dev-ops-config :only[delivery-settings]]
        [pallet.actions :only[package remote-file]]
        [pallet.api :only[server-spec plan-fn]]))


;; landing site business server (lsbs)
(def lsbs-app-jar (server-spec :phases {:configure (plan-fn (remote-file (:remote-jar delivery-settings)
                                                                      {:local-file (:local-jar delivery-settings)
                                                                      :owner (:owner delivery-settings)}))}))

;; debian packages
(def with-daemontools (server-spec :phases {:configure (plan-fn (package "daemontools"))}))
(def with-rsync (server-spec :phases {:configure (plan-fn (package "rsync"))}))
(def with-bzip2 (server-spec :phases {:configure (plan-fn (package "bzip2"))}))
(def with-mysql-client (server-spec :phases {:configure (plan-fn (package "mysql-client"))}))
                                      
  
    
