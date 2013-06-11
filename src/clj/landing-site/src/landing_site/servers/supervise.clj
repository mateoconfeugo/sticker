(ns landing-site.server.supervise
  "Provision and install landing site optimization server"
  (:use[landing-site.config :only [delivery-settings]]
       [pallet.actions :only [package rsync-directory exec-scripts*]]
       [pallet.api :only [server-spec plan-fn]]))


(def install-supervise  (server-spec :phases {:install (plan-fn
                                                        (package "rsync")
                                                        (package "daemontools"))}))

(def supervise-settings (server-spec :phases {:settings (plan-fn
                                                         (rsync-directory
                                                          (:rsync-from-dir delivery-settings)
                                                          (:rsync-to-dir delivery-settings)
                                                          (:owner delivery-settings)))}))

(def supervise-spec (server-spec :extends [install-supervise supervise-settings]))

(def supervise-lsbs (server-spec :extends [supervise-spec]
                                :phases {:run (plan-fn (exec-script* (:supervised-lsbs-cmd delivery-settings)))}))

(def supervise-monitoring (server-spec :extends [supervise-spec]
                                       :phases {:run (plan-fn (exec-script* (:supervised-monitoring-cmd delivery-settings)))}))


