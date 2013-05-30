(use pallet.task.database
     "Deploy landing site database server and dependencies into an infrastructure environment"
     (:use [pallet.api :only [group-spec converge]]
           [pallet.configure :only [compute-service]]
           [landing-site.groups.delivery-crate :only[]]))

(defn provision-db-server
  "Create the database server nodes install mysql and load the schema"
  [{:keys [group-name schema-file db-username db-password db-name
           owner provider node-spec node-count db-group-spec] :as settings}]
  (let [db-server-spec (lsbs-mysql-db-spec settings)
        db-provider-host (pallet.configure/compute-service provider)]
    (future (pallet.api/converge  {db-group-spec node-count} :compute db-provider-host))))
