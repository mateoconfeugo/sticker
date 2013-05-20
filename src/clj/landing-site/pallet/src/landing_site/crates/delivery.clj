(ns landing-site.crates.delivery
  "Provision and install landing site optimization delivery server"
  (:use
   [clojure.core]
   [pallet.configure :only [compute-service defpallet]]     
   [pallet.api :only [plan-fn node-spec server-spec group-spec]]
   [pallet.crate :only [defplan]]
   [pallet.action :only [with-action-options]]
   [pallet.actions :only [package remote-file rsync-directory exec-script*]]
   [landing-site.servers [cms :only[create-cms-lsbs]
                          monitoring :only [create-monitoring-server]]))

  ;; TODO: Look at moving supervisor scripts to resource directory remove silly rsync dependency
(defplan create-delivery-lsbs-spec
  "Download the compojure based standalone jar rsync and daemontools
   use rsync to update the supervisor scripts"
  [{:keys [monitor-host db-host db-name db-username db-password node-count owner group
           local-jar remote-jar rsync-from rsync-to root-dir cfg-dir website-dir ] :as settings}]
  
  (let [lsbs-jar-spec (server-spec :phases {:configure (plan-fn (remote-file remote-jar :local-file local-jar :owner owner))})
        daemontools-pkg-spec  (server-spec :phases {:configure (plan-fn (package "daemontools"))})
        rsync-pkg-spec  (server-spec :phases {:configure (plan-fn (package "rsync"))})
        rsync-spec (server-spec :phases {:configure (plan-fn (rsync-directory rsync-from rsync-to))})        
        dependencies [rsync-pkg-spec daemontools-pkg-spec lsbs-jar-spec rsync-spec]]
  (server-spec :extends dependencies)))

(defplan launch-lsbs-spec
  "Start the landing site application from a jar under daemontools supervisor"
  [{:keys [db-host db-name db-username db-password owner group provider
           root-dir cfg-dir website-dir monitor-host] :as settings}]
  (let [app-launch-cmd  "nohup java -jar /home/pallet-admin/landing-site.jar"
        launch-env {:script-env {:LSBS_CFG_DIR cfg-dir :LSBS_WEBSITE website-dir :LSBS_ROOT_DIR root-dir
                                 :LSBS_DB_ADDRESS db-host :LSBS_MONITORING_ADDRESS monitor-host :LSBS_DB_NAME db-name
                                 :LSBS_DB_USER db-username :LSBS_DB_PASSWORD db-password} }])
  (server-spec :phases {:configure (plan-fn (with-action-options launch-env (exec-script* app-launch-cmd)))})