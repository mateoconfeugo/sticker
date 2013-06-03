(use pallet.task.delivery
     "Deploy landing site database server and dependencies into an infrastructure environment"
     (:use [pallet [api :only [group-spec converge]
                    configure :only [compute-service]
                    actions :only [set-force-overwrite]]]
           [landing-site [crates.delivery :only [create-delivery-lsbs-spec launch-lsbs-spec]
                          groups.delivery :only[]
                          config :only [delivery-settings db-settings]]]))

(defn provision-delivery-server 
  "Setup, install, configure run the delivery servers"
  [{:keys [db-host db-name db-username db-password owner group provider
           local-jar remote-jar rsync-from rsync-to group-spec-name
           root-dir cfg-dir website-dir cms-local-dir cms-remote-dir
           dist-url node-spec monitoring-cfg-dir node-count monitor-host] :as settings}]
  (let [provider-host (compute-service provider)                
        infrastructure (group-spec group-spec-name :node-spec delivery-node-spec :extends [base-lsbs-server-spec])
        _ (set-force-overwrite 1)
        lsbs-nodes (future (converge  {infrastructure node-count} :compute provider-host :phase [:settings :install :configure :run]))
        lsbs-node  ((first (@lsbs-nodes :targets)) :node)
        lsbs-delivery-group-spec (group-spec group-spec-name :node-spec delivery-node-spec :extends [(launch-lsbs-spec settings)])
        delivery-launch-results (:results (future (lift delivery-server-spec :compute provider-host)))]
    lsbs-nodes))

(defn system-startup
  "Create the whole landing site system: biz, db, monitoring servers"
  [{:keys db-settings delivery-settings}]
  (let [db-nodes (provision-db-server db-settings)
        db-node  ((first (@db-nodes :targets)) :node)
        db-ip (.primary-ip db-node)
        lsbs-nodes (provision-delivery-server (assoc delivery-settings :db-host db-ip))
        lsbs-node  ((first (@lsbs-nodes :targets)) :node)]
    (.primary-ip lsbs-node)))