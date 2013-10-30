(ns test.site-builder
  (:require [com.ashafa.clutch :refer [put-document get-document all-documents delete-database update-document with-db]]
            [expectations :refer [expect]]
            [site-builder-udc.db :refer [site-builder-db]]
            [site-builder-udc.site-builder :refer [get-new-landing-site-id update-tmp-xpath-uuid-index update-landing-site save-landing-site]]
            [clojure.edn :refer [read-string]]
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]
            [clj-webdriver.taxi :as scraper :refer [set-driver! to click exists? input-text submit quit page-source]]
            [site-builder-udc.config :refer [apply-site-builder-configurations]]
            [site-builder-udc.db :refer [site-builder-db
                                         site-builder-resource
                                         get-site-builder-db-host
                                         get-site-builder-db-port
                                         get-site-builder-db-username-prefix]]))
;;========================================================================
;; SETUP
;;========================================================================
(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/test_mgmt"
               :user "root"
               :password "test123"})

(def test-user (first (jdbc/with-connection mysql-db
  (jdbc/with-query-results res ["select * from user where id =?" 1]
    (doall res)))))

(def snippet-html "<div id='test-dom'>barry</div>")
(def layout "<html><body><div id='host'>foo</div></body></html>")
(def xpath "/html/body/div")
(def ls-id 10)
(def uuid 1)

(def ^:dynamic *cfg* (apply-site-builder-configurations (clojure.edn/read-string (slurp "resources/config.edn"))))

(binding [*cfg* (apply-site-builder-configurations (clojure.edn/read-string (slurp "resources/config.edn")))]
  (let [prefix (get-site-builder-db-username-prefix)]
    prefix))

(def doc-db (site-builder-db (:username  test-user) (:db-name-prefix *cfg*)))

;;========================================================================
;; UNIT TEST: User with no landing sites generates an id with value 1
;;========================================================================
(expect true (= 1 (get-new-landing-site-id doc-db)))

;;========================================================================
;; UNIT TEST: User with landing sites increments the max number
;;========================================================================
(put-document doc-db {:_id "10"})
(put-document doc-db {:_id "11"})
(put-document doc-db {:_id "13"})
(expect true (= 14 (get-new-landing-site-id doc-db)))

;;========================================================================
;; UNIT TEST: The xpath-uuid index map has the correct snippet-html
;;========================================================================
(def updated-doc @(future (update-tmp-xpath-uuid-index {:db doc-db :landing-site-id 10 :xpath xpath :uuid uuid :snippet-html snippet-html})))
(def test-doc @(future (get-document doc-db 10)))
(expect true (= (get-in test-doc [:tmp-xpath-uuid (keyword (str xpath "-" uuid))]) snippet-html))

;;========================================================================
;; UNIT TEST:  Updating a landing site gets reflected through to the data store
;;========================================================================
(update-landing-site {:db doc-db :landing-site-id 10 :xpath xpath :uuid uuid :snippet-html snippet-html :page-html layout})
(expect true (= (get (get-document doc-db 10) :tmp-page-html) layout))

;;========================================================================
;; UNIT TEST:  Save landing site move the edits from tmp storage permanent
;;========================================================================
(save-landing-site {:db doc-db :landing-site-id 10 :page-html layout})
(expect true (= (get (get-document doc-db 10) :page-html) layout))

;;========================================================================
;; UNIT TEST:  Publish landing site to heroku
;;========================================================================
(def test-destination nil)
(def landing-site (publish-landing-site {:landing-site-id 10 :destination-id 1}))
(def published-site-uri (:published-uri landing-site))
(scraper/set-driver! {:browser :firefox} published-site-uri)
(expect true (= (scraper/page-source) layout))

;;========================================================================
;; TEARDOWN:
;;========================================================================
(delete-database doc-db)
