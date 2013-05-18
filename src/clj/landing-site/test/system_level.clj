(comment
    "System Level tests need to create the virtual environment that allows all
   the services that the landing site needs to created and running as it would
   in production just at a smaller scale
  High level acceptance tests that make sure business level functionality is achieved
   ACCEPTANCE TESTS:
     * User via web browser navigates to landing site enters a valid lead and it got stored in database")

(ns system-level
  (:require
   [pallet.core :only [group-spec node-spec ]]
   [pallet.configure :only [compute-service defpallet]]     
   [pallet.api :only [plan-fn]]
   [landing-site.handler]
   [pallet.core.session]
   [pallet.crate.environment]
   [pallet.crate.app-deploy :as app-deploy]
   [pallet.crate.runit :as runit]
   [pallet.crate.riemann]
   [pallet.script :as script]
   [pallet.action :only[ with-action-options]]
   [pallet.actions :only [package set-force-overwrite remote-file remote-directory rsync-directory]])
;;   [pallet.compute.vmfest.service]
  (:use  [expectations]
         [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]
         [clojure.core]))

(def lsbs-upstart "upstart.conf")

(def remote-jar "/home/pallet-admin/landing-site.jar")
(def local-jar  "/Users/matthewburns/github/florish-online/src/clj/landing-site/target/landing-site-0.1.0-standalone.jar")

(def remote-cms-dir "/home/pallet-admin/website")
(def local-cms-dir "/Users/matthewburns/github/florish-online/src/clj/landing-site/test/site.tgz")

(def vmfest (pallet.configure/compute-service "vmfest"))
(defpallet :services {:vmfest {:provider "vmfest"}})

(def delivery-node-spec (pallet.api/node-spec :image {:image-id :java-mysql-postfix}))

(defn create-lsbs-user [username]
  (pallet.api/server-spec
   :phases {:configure
            (pallet.api/plan-fn (pallet.actions/user username {:action :create :shell :bash :create-home 1 :home-dir "/home"}))}))

;; Provision and install landing site database
(def schema-path "/Users/matthewburns/github/florish-online/src/clj/landing-site/landing-site.sql")
(def landing-site-schema (slurp schema-path))
(def lsbs-db-user "root")
(def lsbs-db-password "test123")
(def lsbs-db-name "pcs")
(def load-landing-database-cmd (str "mysql  -u " lsbs-db-user  " -p"lsbs-db-password " " lsbs-db-name " < " "landing-site.sql"))
(def create-landing-database-cmd (str "mysqladmin  -u " lsbs-db-user  " -p"lsbs-db-password  " create " lsbs-db-name))
(def mysql-client (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn ((pallet.actions/package "tree")))}))
(def load-lsbs-database (pallet.api/server-spec
                         :phases {:configure (pallet.api/plan-fn
                                              (do
                                                (pallet.actions/remote-file "landing-site.sql" :local-file  schema-path :owner "pallet-admin")
                                                (pallet.actions/exec-script* create-landing-database-cmd)                                                
                                                (pallet.actions/exec-script* load-landing-database-cmd)
                                                ))}))

(def lsbs-dependencies [mysql-client load-lsbs-database])
(def lsbs-database (pallet.api/group-spec "lsbs-database" :node-spec delivery-node-spec :extends [load-lsbs-database]))
(def lsbs-database-tmp (pallet.api/group-spec "lsbs-database" :node-spec delivery-node-spec :extends [mysql-client]))
(def db-provider-host (pallet.configure/compute-service :vmfest))
(def db-nodes (future (pallet.api/converge  {lsbs-database 1} :compute db-provider-host)))
;;(def db-nodes (pallet.api/converge  {lsbs-database 0} :compute db-provider-host))
(def db-node  ((first (@db-nodes :targets)) :node))
(def db-ip (.primary-ip db-node))

;; Provision and start landing site business server
(def cms-resources   (pallet.api/server-spec
                      :phases {:configure (pallet.api/plan-fn (pallet.actions/remote-directory remote-cms-dir
                                                                                               :local-file  local-cms-dir
                                                                                               :owner "pallet-admin"
                                                                                               :group "pallet-admin"
                                                                                               :unpack :tar))}))

(defn copy-landing-site-jar
  [{:keys [local-jar remote-jar owner]}]
  (pallet.api/server-spec
   :phases {:configure
            (pallet.api/plan-fn (pallet.actions/remote-file remote-jar :local-file  local-jar :owner owner))}))

(def with-landing-site-jar (copy-landing-site-jar {:local-jar local-jar :remote-jar remote-jar :owner "pallet-admin"}))

(defn rsync-resource-dir
  [{:keys [from to owner]}]
  (pallet.api/server-spec
   :phases {:configure
            (pallet.api/plan-fn (pallet.actions/rsync-directory from to))}))

(def supervise-dir-path  "/Users/matthewburns/github/florish-online/supervise")
(def with-supervisor-scripts (rsync-resource-dir {:from supervise-dir-path :to "/home/pallet-admin" }))
(def daemontools  (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn (pallet.actions/package "daemontools"))}))
(def rsync  (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn (pallet.actions/package "rsync"))}))
;;(def export-cfg-env-path (pallet.api/server-spec
;;                         :phases {:configure (pallet.api/plan-fn (pallet.actions/exec-script* "export LSBS_CFG_DIR=/home/pallet-admin/website/config"))}))

(def export-cfg-env-path (pallet.api/server-spec
   
                                                   :phases {:configure (pallet.api/plan-fn (pallet.actions/exec-script* "export LSBS_CFG_DIR=/home/pallet-admin/website/config"))}))
                       ;;                         :phases {:configure (pallet.api/plan-fn (pallet.crate.environment/system-environment ["LSBS_CFG_DIR" "/home/pallet-admin/website/config"]))}))

;;(def app-launch-cmd  "supervise /home/pallet-admin/supervise/landing_site")
;;(def app-launch-cmd  "nohup java -jar /home/pallet-admin/landing-site.jar 2>&1 &")
(def app-launch-cmd  "nohup java -jar /home/pallet-admin/landing-site.jar")
(def test-env {:script-env {:LSBS_CFG_DIR "/home/pallet-admin/website/config"
                            :LSBS_WEBSITE "/home/pallet-admin/website"
                            :LSBS_ROOT_DIR "/home/pallet-admin"
                            :LSBS_DB_ADDRESS db-ip
                            :LSBS_MONITORING_ADDRESS "127.0.0.1"
                            :LSBS_DB_NAME "pcs"
                            :LSBS_DB_USER "root"
                            :LSBS_DB_PASSWORD "test123"} })

(def start-landing-site (pallet.api/server-spec
                         :phases {:configure (pallet.api/plan-fn (pallet.action/with-action-options
                                                                  test-env (pallet.actions/exec-script* app-launch-cmd)))}))

(def deployment-settings {:app-root "/home/pallet-admin"
                          :artifacts {:from-lein
                                      [{:project-path "target/landing-site-0.1.0-standalone.jar"
                                                :path "landing-site.jar"}]}
                          :run-command app-launch-cmd
                          :user "pallet-admin"
                          :instance-id :pcs-landing-site})

(def deploy-lsbs (pallet.crate.app-deploy/server-spec deployment-settings))
(def runit  (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn (pallet.actions/package "runit"))}))
(def with-runit (pallet.crate.runit/server-spec {}))

(def dependencies [cms-resources rsync with-supervisor-scripts])
(def lsbs-infrastructure (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec :extends dependencies))
(def riemann-cfgs-dir   "/Users/matthewburns/github/florish-online/conf")
(def run-riemann-cmd "nohup /opt/riemann/bin/riemann /opt/riemann/etc/riemann.config")
(def with-riemann (pallet.api/server-spec :phases {:configure (pallet.api/plan-fn (pallet.action/with-action-options {:sudo 1}
                                                                                    (do
;;                                                                                      (pallet.actions/package "bzip2")
                                                                                    (pallet.actions/exec-script* run-riemann-cmd))))}))
                 


(def monitoring (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec :extends [(pallet.crate.riemann/server-spec {:dist-url "http://aphyr.com/riemann/riemann-0.2.1.tar.bz2" })  with-riemann]))
;;:user "pallet-admin"
;;                                                                                                                                  :group "pallet-admin"
;;                                                                                                                                  :owner "pallet-admin"} )]))

(def landing-sites (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec :extends [start-landing-site]))
;;(def landing-sites (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec :extends [with-landing-site-jar start-landing-site]))

(pallet.actions/set-force-overwrite 1)
(def lsbs-nodes (pallet.api/converge  {landing-sites 0} :compute vmfest))
(def lsbs-nodes (future (pallet.api/converge  {lsbs-infrastructure 1} :compute vmfest)))
(def lsbs-node  ((first (@lsbs-nodes :targets)) :node))
(def lsbs-ip (.primary-ip lsbs-node))

(def monitoring-results (:results (future (pallet.api/lift  monitoring :compute vmfest))))
(def monitoring-results (:results (future (pallet.api/lift  monitoring :compute vmfest :phase [:settings :install :configure :run]))))
(def launch-results (:results (future (pallet.api/lift  landing-sites :compute vmfest))))
;;(def launch-results (:results (future (pallet.api/lift  monitoring :compute vmfest :phase [:install :settings :configure :run]))))
;;(def launch-results (:results (future
;;                                (pallet.api/with-admin-user "pallet-admin" (pallet.api/lift  monitoring :compute vmfest )))))


(defn wait-sec [n] (Thread/sleep (* 1000 n)))




;; Need wait while the machine boots up if in that mode
;;(def test-target-node  ((first (@lsbs-node :targets)) :node))
;;(def target-ip (.primary-ip test-target-node))

(def test-port 8087)
;;(def test-uri "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view")
(def test-uri "/")
;;(def app-server (landing-site.handler/start test-port))
(def test-lead {:email "matthewburns@gmail.com" :full_name "bob smit" :phone "8182546028"})
;; Start up a browser
(def app-uri (str "http://" lsbs-ip ":" test-port test-uri))
(scraper/to app-uri)
(scraper/set-driver! {:browser :firefox} app-uri)
;; input lead info
(scraper/input-text "#lead_full_name" (:full_name test-lead) )
(scraper/input-text "#lead_phone" (:phone test-lead))
(scraper/input-text "#lead_email" (:email test-lead))
(scraper/click "#lead-form-submit")
(scraper/quit)
(.stop app-server)
(def prev-record-id (:id (parse-string (:body test-response) true)))
(def current-id (inc prev-record-id))
(def db-record (first (kdb/select lead_log
                                  (kdb/fields :id :full_name :email :phone :postal_code)
                                  (kdb/where {:id current-id}))))

;; ACCEPTANCE TEST: User entered a valid lead and it got stored
(expect true (= (:full_name test-lead) (:full_name db-record)))

;; CLEANUP
(kdb/delete lead_log
            (kdb/where {:id prev-record-id}))
(kdb/delete lead_log
            (kdb/where {:id (:id db-record)}))


(power-down lsbs-provider-host)
(destroy lsbs-provider-host)

(comment
(def my-server (server "http://localhost:18083"))
(def my-machine
  (instance my-server "my-vmfest-vm"
            {:uuid "/Users/matthewburns/.vmfest/models/java-mysql-postfix.vdi"}
            {:memory-size 512
             :cpu-count 1
             :storage [{:name "IDE Controller"
                        :bus :ide
                        :devices [nil nil {:device-type :dvd} nil]}]
             :boot-mount-point ["IDE Controller" 0]
             :network [{:attachment-type :bridged
                        :bridged-interface "en1: dkw (AirPort)"}]})))
