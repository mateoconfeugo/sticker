(ns test.controllers.api
  (:require [com.ashafa.clutch :refer [put-document get-document all-documents delete-database update-document with-db]]
            [expectations :refer [expect]]
            [site-builder-udc.controllers.api :refer [save update]]
            [site-builder-udc.db :refer [site-builder-db]]            
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]))

;;========================================================================
;; SETUP:
;;========================================================================
(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/test_mgmt"
               :user "root"
               :password "test123"})

(def test-user (first (jdbc/with-connection mysql-db
  (jdbc/with-query-results res ["select * from user where id =?" 1]
    (doall res)))))

(def dom "<div id='test-dom'>funky</div>")
(def layout "<html><body><div id='host'>slappy</div></body></html>")
(def xpath "/html/body/div")
(def ls-id 10)
(def uuid 1)

(def doc-db (site-builder-db test-user))

;;========================================================================
;; UNIT TEST: Client facing interface updates server with edits these edits
;; are stored in a temporary field
;;========================================================================
(put-document doc-db {:_id "10"})
(def client-side-data {:xpath xpath :page-html layout :dom dom  :landing-site-id ls-id :user-id 1})
(def landing-site (update client-side-data))
(expect true (= layout (:page-html landing-site)))

;;========================================================================
;; UNIT TEST: Client facing interface that saves the page html and moves
;; the temporary updates into the permanant storage and removes the temporary
;; store.
;;========================================================================
(def client-side-data {:page-html layout :landing-site-id ls-id :user-id 1})
(def landing-site (save client-side-data))
(expect true (= layout (:page-html landing-site)))

;;========================================================================
;; TEAR DOWN:
;;========================================================================
(delete-database doc-db)
                



  