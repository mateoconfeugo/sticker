(use pallet.task.monitoring
     "Deploy landing site database server and dependencies into an infrastructure environment"
     (:use [pallet.api :only [converge ]]
           [pallet.configure :only [compute-service]]
           [landing-site.groups.monitoring :only[base-monitoring-group-spec]]))

(defn provision-monitoring-server
  "Create the database server nodes install mysql and load the schema"
  [{:keys [group-name schema-file db-username db-password db-name
           owner provider node-spec node-count db-group-spec] :as settings}]
  (let[provider-host (compute-service provider)]                
    (future (converge  {base-monitoring-group-spec node-count} :compute provider-host))))
