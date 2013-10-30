(ns site-builder.test.views.editor
  (:require [clojure.core]
            [clojure.java.jdbc :as sql :refer [with-connection create-table]]
            [clj-webdriver.taxi :as scraper  :refer [set-driver! to click exists? drag-and-drop css-finder
                                                     input-text submit quit present?]]
            [com.ashafa.clutch :as clutch :refer [get-document]]
            [expectations]
            [site-builder-udc.views.editor]
            [flourish-common.db :refer [initialize-management-database drop-database create-database]]))


;;            [pallet.action :only[with-action-options]]
;;            [pallet.configure :only[compute-service]]
;;            [pallet.api :only[converge]]
;;            [site-builder-udc.handler]
;;            [site-builder-udc.dev-ops-config]
;;            [site-builder-udc.groups.database :refer[database-group]]
;;            [site-builder-udc.groups.management :refer [management-group]]


(defn setup
  []
  "Create and populate a test management database"
  (let [db-name "mgmt"
        db-port 3306
        db-host "localhost"
        db-user "root"
        db-password "test123"
        db-spec   {:classname "com.mysql.jdbc.Driver"
                   :subprotocol "mysql"
                   :subname "//127.0.0.1:3306/test_mgmt"
                   :user "root"
                   :password "test123"}]
    (do (create-database db-spec "test_mgmt")
        (initialize-management-database db-spec)
        (sql/with-connection db-spec
          (sql/insert-records :user
                               {:id 1 :username "bob" :password "test123"}
                               {:id 2 :username "sally" :password "test123"}
                               {:id 3 :username "mary" :password "test123"})))))

;;====================================================================
;; Spin up a sitebuilder running on a virtual machines to test against
;;====================================================================
;;(def service (compute-service (:provider db-settings)))
;;(def site-builder-nodes (converge {database-group 1} :compute service))
;;(def site-builder-node  ((first (@db-nodes :targets)) :node))
;;(def site-builder-ip (.primary-ip db-node))
(def site-builder-ip  "localhost")

;;====================================================================
;; Setup variables to perform the drag and drop editing tests
;;====================================================================

(def test-uuid 1)
(def test-port 3000)
(def test-uri "/editor")
(def app-uri (str "http://" site-builder-ip ":" test-port test-uri))

;;====================================================================
;; Spin up a web browser to perform the use case/feature acceptance tests
;;====================================================================
(setup)
(def from-selector "span.drag")
(def to-selector "div.hero-unit")

(scraper/set-driver! {:browser :firefox} app-uri)
(scraper/to app-uri)

(scraper/drag-and-drop from-selector to-selector)
;;(scraper/drag-and-drop ".drag.label" ".hero-unit")
(scraper/click "#save-landing-site")
(scraper/quit)
(.stop app-server)
;;====================================================================
;; Check to see if the content of the web browser has changed
;;====================================================================
(scraper/present? (str "uuid_" test-uuid))
;;====================================================================
;; Check to see if the landing-site document has been editted
;;====================================================================
;;====================================================================
;; ACCEPTANCE TEST: User entered a valid lead and it got stored
;;====================================================================
;;(expect true (= (:full_name test-lead) (:full_name db-record)))

;; CLEANUP AND POWER DOWN VIRTUAL MACHINES
;; TODO Make this conditional
;;(power-down site-builer-provider-host)
;;(destroy site-builder-provider-host)




