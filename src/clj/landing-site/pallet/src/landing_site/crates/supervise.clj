(ns landing-site.crates.supervise
  (:use   [pallet.api :only [plan-fn server-spec]]         
          [pallet.actions :only [package rsync-directory]]
          [pallet.crate :only [defplan]]))

(defplan setup-supervisor
  "setup the supervision and control of all the system servers"
  [{:keys [from to owner]}]
  (server-spec :phases {:configure
            (plan-fn
             (package "rsync")
             (rsync-directory from to)
             (package "daemontools"))}))