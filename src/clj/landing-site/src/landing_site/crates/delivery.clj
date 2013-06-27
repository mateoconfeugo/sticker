(ns landing-site.crates.delivery
  "Provision and install landing site optimization delivery server"
  (:import [java.io.File])
  (:use [clostache.parser :only [render-resource]]
        [clojure.java.shell :only[sh]]
        [landing-site.crates.cms :only[rsync-with-cms setup-user-on-cms]]
        [landing-site.servers.delivery :only[target-delivery-spec]]
        [pallet.api :only [plan-fn server-spec make-user lift group-spec]]
        [pallet.crate :only [defplan]]
        [pallet.action :only [with-action-options]]
        [pallet.actions :only [exec-script* remote-file user exec-script directory]]))

;;(str "nohup java -jar " root-dir "/landing-site.jar")
(defplan launch-lsbs
  "Start the landing site application from a jar under daemontools supervisor"
  [{:keys [db-host db-name db-username db-password owner group provider
           root-dir cfg-dir website-dir monitor-host] :as settings}]
  (let [app-launch-cmd  (str "svc -u " root-dir "/supervise/landing-site")
        launch-env {:script-env {:LSBS_CFG_DIR cfg-dir :LSBS_WEBSITE website-dir :LSBS_ROOT_DIR root-dir
                                 :LSBS_DB_ADDRESS db-host :LSBS_MONITORING_ADDRESS monitor-host :LSBS_DB_NAME db-name
                                 :LSBS_DB_USER db-username :LSBS_DB_PASSWORD db-password} }]
    (server-spec :phases {:configure (plan-fn (with-action-options launch-env (exec-script* app-launch-cmd)))})))

(defplan setup-user-on-target
  [{:keys [username  public-key private-key app-cfg-path website-dir db-address
           db-password db-name monitoring-uri db-user  owner group] :as settings}]
  "Create the user one the target with the right ssh key pair
   create the correct .profile so as to set the user environment
   that the systems runs in correctly"
  (let [home-dir (str "/home/" username)
        dir-exists? (str "if [ -d " home-dir " ]; then exit 1; fi")]
    (server-spec :phases {:configure (plan-fn
                                      (do
                                        (try (user username :action :remove :force true) (catch Exception e))
                                        (try (exec-script* (str "rm -rf " home-dir)) (catch Exception e))
                                        (try (exec-script* (str "useradd  -s /bin/bash -m  -d /home/" username  " " username))
                                             (catch Exception e))
                                        (try
                                          (remote-file (str "/home/" username "/.profile")
                                                       :owner username :group username :mode "0644"
                                                       :action :create  :overwrite-changes true
                                                       :no-versioning true
                                                       :content (render-resource "templates/.profile.tmpl"
                                                                                 {:app-cfg-path app-cfg-path
                                                                                  :website-dir website-dir
                                                                                  :db-address db-address
                                                                                  :db-user db-user
                                                                                  :db-password db-password
                                                                                  :db-name db-name
                                                                                  :username username
                                                                                  :monitoring-uri monitoring-uri}))
                                          (catch Exception e))
                                        (directory  (str "/home/" username "/.ssh") :action :create
                                                    :owner username :group username :mode "0700")
                                        (remote-file (str "/home/" username "/.ssh/id_rsa")
                                                     :owner username :group username :mode "0600"
                                                     :action :create :force true
                                                     :content private-key)
                                        (remote-file (str "/home/" username "/.ssh/id_rsa.pub")
                                                     :owner username :group username :mode "0644"
                                                     :action :create :force true
                                                     :content  public-key)))})))
(comment
(defplan install-delivery
  [{:keys [username hostname tmp-dir cms-target delivery-target] :as settings}]
  "Encapsulate the ability to spin up a cluster that
   has the whole system going on previously exiting nodes"
  (let [ssh-info ( username hostname tmp-dir)]
    [(lift (group-spec "flourish-ls" :extends [(setup-user-on-target (merge ssh-info settings))
                                                (rsync-with-cms  settings)
                                                target-delivery-spec]) :compute delivery-target)
     (lift (group-spec "flourish-ls" :extends [(setup-user-on-cms (merge ssh-info settings))])
            :compute cms-target)]))
)
;;(defplan install-qa-delivery [settings]
;;  (install-user-delivery (assoc settings :cms-target production-cms :delivery-target qa-release-target)))
