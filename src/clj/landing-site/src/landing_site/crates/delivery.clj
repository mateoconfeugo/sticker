(ns landing-site.crates.delivery
  "Provision and install landing site optimization delivery server"
  (:use [pallet.api :only [plan-fn server-spec ]]
        [pallet.crate :only [defplan]]
        [pallet.action :only [with-action-options]]
        [pallet.actions :only [exec-script*]]
        [landing-site.servers [cms :only[create-cms-lsbs]
                               monitoring :only [create-monitoring-server]
                               packages [with-rsync with daemontools app-jar]))

 ;; TODO: Look at moving supervisor scripts to resource directory remove silly rsync dependency
(defplan launch-lsbs
  "Start the landing site application from a jar under daemontools supervisor"
  [{:keys [db-host db-name db-username db-password owner group provider
           root-dir cfg-dir website-dir monitor-host] :as settings}]
  (let [app-launch-cmd  "nohup java -jar /home/pallet-admin/landing-site.jar"
        launch-env {:script-env {:LSBS_CFG_DIR cfg-dir :LSBS_WEBSITE website-dir :LSBS_ROOT_DIR root-dir
                                 :LSBS_DB_ADDRESS db-host :LSBS_MONITORING_ADDRESS monitor-host :LSBS_DB_NAME db-name
                                 :LSBS_DB_USER db-username :LSBS_DB_PASSWORD db-password} }])
  (server-spec :phases {:configure (plan-fn (with-action-options launch-env (exec-script* app-launch-cmd)))})