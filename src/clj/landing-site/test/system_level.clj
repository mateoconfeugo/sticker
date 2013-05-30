(comment
    "System Level tests need to create the virtual environment that allows all
   the services that the landing site needs to created and running as it would
   in production just at a smaller scale
  High level acceptance tests that make sure business level functionality is achieved
   ACCEPTANCE TESTS:
     * User via web browser navigates to landing site enters a valid lead and it got stored in database")

(ns system-level
  (:use [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]
        [landing-site.handler]
        [landing-site.config :only [db-settings delivery-settings]]
        [landing-site.groups.database :only[database-group]]
        [landing-site.groups.delivery :only[delivery-group]]        
        [expectations]
        [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]
        [clojure.core]))

;; Spin up a system of virtual machines to test with
(def service (compute-service (:provider db-settings)))
(def db-nodes (converge {database-group 1} :compute service))
(def db-node  ((first (@db-nodes :targets)) :node))
(def db-ip (.primary-ip db-node))
(def monitoring-nodes (converge {monitoring-group 1} :compute service))
(def monitoring-node  ((first (@db-nodes :targets)) :node))
(def monitoring-ip (.primary-ip db-node))
(def delivery-nodes (converge {delivery-group 1} :compute service))
(def delivery-node  ((first (@db-nodes :targets)) :node))
(def delivery-ip (.primary-ip db-node))

(defn wait-sec [n] (Thread/sleep (* 1000 n)))
(def test-port 8087)
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
