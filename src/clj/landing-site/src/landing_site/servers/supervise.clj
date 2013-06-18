(ns landing-site.servers.supervise
  "Provision and install landing site optimization server"
  (:use [landing-site.dev-ops-config :only [delivery-settings]]
        [pallet.actions]        
        [pallet.api :only [server-spec plan-fn]]))


(def install-supervise  (server-spec :phases {:configure (plan-fn
                                                          (package "daemontools"))}))
                                                          

;;(def supervise-settings (server-spec :phases {:settings (plan-fn
;;                                                         (remote-file
;;                                                          (:rsync-from delivery-settings)
;;                                                          :local-file (:rsync-to delivery-settings)))}))

(comment
                                                           (remote-directory
                                                          "/home/pallet-admin/supervise/landing-site/run"
                                                          :owner "pallet-admin", :group "pallet-admin" :mode "0644"
                                                          :action :create :force true
                                                          :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/resources/run")
)

(def supervise-settings (server-spec :phases {:settings (plan-fn

                                                         (remote-directory
                                                          "/home/pallet-admin/config/site-config.json"
                                                          :owner "pallet-admin", :group "pallet-admin" :mode "0644"
                                                          :action :create :force true
                                                          :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/resources/site-config.json")
                                                         )}))



(def supervise-spec (server-spec :extends [install-supervise supervise-settings]))
;;(def supervise-spec (server-spec :extends [install-supervise]))

(def supervise-lsbs (server-spec :extends [supervise-spec]
                                :phases {:run (plan-fn (exec-script* (:supervised-lsbs-cmd delivery-settings)))}))

(def supervise-monitoring (server-spec :extends [supervise-spec]
                                       :phases {:run (plan-fn (exec-script* (:supervised-monitoring-cmd delivery-settings)))}))


