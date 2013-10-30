(ns management.servers.packages
  "packages needed by the different servers"
  (:require [management.dev-ops-config :refer [management-settings]]
            [pallet.actions :refer [package remote-file]]
            [pallet.api :refer [server-spec plan-fn]]))

;; debian packages
(def with-daemontools (server-spec :phases {:configure (plan-fn (package "daemontools"))}))
(def with-rsync (server-spec :phases {:configure (plan-fn (package "rsync"))}))
(def with-bzip2 (server-spec :phases {:configure (plan-fn (package "bzip2"))}))
(def with-mysql-client (server-spec :phases {:configure (plan-fn (package "mysql-client"))}))
(def with-mysql-server (server-spec :phases {:configure (plan-fn (package "mysql-server"))}))
(def with-ftp-client (server-spec :phases {:configure (plan-fn (package "ftp"))}))
