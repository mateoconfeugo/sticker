(ns test.site-builder
  (:require [com.ashafa.clutch :refer [put-document get-document all-documents delete-database update-document with-db]]
            [expectations :refer [expect]]
            [site-builder-udc.db :refer [site-builder-db]]            
            [site-builder-udc.site-builder :refer [get-new-landing-site-id update-tmp-xpath-uuid-index update-landing-site save-landing-site]]
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]
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

(def dom "<div id='test-dom'>barry</div>")
(def layout "<html><body><div id='host'>foo</div></body></html>")
(def xpath "/html/body/div")
(def ls-id 10)
(def uuid 1)
(def doc-db (site-builder-db test-user))

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
;; UNIT TEST: The xpath-uuid index map has the correct dom
;;========================================================================
(update-tmp-xpath-uuid-index {:db doc-db :landing-site-id 10 :xpath xpath :uuid uuid :dom dom})
(def test-doc (get-document doc-db 10))
(expect true (= (get-in test-doc [:tmp-xpath-uuid (keyword (str xpath "-" uuid))]) dom))

;;========================================================================
;; UNIT TEST:  Updating a landing site gets reflected through to the data store
;;========================================================================
(update-landing-site {:db doc-db :landing-site-id 10 :xpath xpath :uuid uuid :dom dom :page-html layout})
(expect true (= (get (get-document doc-db 10) :tmp-page-html) layout))

;;========================================================================
;; UNIT TEST:  Save landing site move the edits from tmp storage permanent
;;========================================================================
(save-landing-site {:db doc-db :landing-site-id 10 :page-html layout})
(expect true (= (get (get-document doc-db 10) :page-html) layout))

;;========================================================================
;; TEARDOWN:
;;========================================================================
(delete-database doc-db)



  

