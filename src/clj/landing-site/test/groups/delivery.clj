(ns groups.delivery
  ^{:author "Matt Burns"
    :doc  "Testing the provisioning code that is responsible for setting up the delviery engine"}
  (:use [expectations]
        [landing-site.config :only[db-settings delivery-settings]]
        [landing-site.core :only[ping]]
        [landing-site.groups.delivery]
        [pallet.api :only [group-spec converge lift]]
        [pallet.configure :only [compute-service]]))

(def ssh-info (ssh-key-paths "pcs" "web100" "."))
;;=======================================================================
;; PROVISIONING TEST ENVIRONMENT
;;=======================================================================
(def pcs-qa {:username "pcs"
             :app-cfg-path "/home/pcs/config/site-config.json"
             :website-dir "/home/pcs/website"
             :db-address "127.0.0.1"
             :db-password "test123"
             :db-user "root"
             :db-name "pcs"
             :monitoring-uri "127.0.0.1"
             :owner "pallet-admin"
             :group "pallet-admin"})

(qa-release {:username "pcs" :host "web100" :tmp-dir "."})
(def user-target-spec (setup-user-on-target (merge ssh-info pcs-qa)))
(def qa-user-setup (group-spec "flourish-ls" :extends [user-target-spec]))
(def qa-cms-server (group-spec "flourish-ls" :extends [cms-server-spec]
                               :compute production-cms))

(def qa-cms-client (group-spec "flourish-ls" :extends [cms-client-spec]
                               :compute qa-release-target))

(def qa-delivery (group-spec "flourish-ls" :extends [target-delivery-spec]
                             :compute qa-release-target))

(def cms-server-results (lift (group-spec "flourish-ls" :extends [cms-server-spec])
                   :compute production-cms))

(def cms-destination-provisioning (lift (group-spec "flourish-ls" :extends [cms-client-spec])
                                        :compute qa-release-target))

(def delivery-server (lift ))

(launch-lsbs delivery-settings)
)


(def service (pallet.configure/compute-service (:provider db-settings)))
(def delivery-nodes (converge {delivery-group 1} :compute service)
(def delivery-node  ((first (@delivery-nodes :targets)) :node))
(def delivery-ip (.primary-ip delivery-node))
(expect true (ping delivery-ip))
