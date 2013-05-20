(ns landing-site.servers.database
   (:use [pallet.api :only [server-spec]]         
         [landing-site.config :only[db-settings]]
         [landing-site.crates.database]))

(def base-database-server-spec (server-spec (:extends (-> db-settings lsbs-mysql-spec)))