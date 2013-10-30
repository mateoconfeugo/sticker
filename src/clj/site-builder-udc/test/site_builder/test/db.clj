(ns test.db
  (:require [com.ashafa.clutch :as clutch]
            [expectations]
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]
            [com.ashafa.clutch :refer [put-document get-document all-documents]]
            [flourish-common.utils :refer [str->int]]
            [site-builder-udc.db :refer [get-landing-sites
                                         site-builder-db
                                         site-builder-resource
                                         get-site-builder-db-host
                                         get-site-builder-db-port
                                         get-site-builder-db-username-prefix]]))


(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/test_mgmt"
               :user "root"
               :password "test123"})

(def test-user (first (jdbc/with-connection mysql-db
  (jdbc/with-query-results res ["select * from user where id =?" 1]
    (doall res)))))

(def doc-db (site-builder-db test-user))
(put-document doc-db {:_id "10"})
(put-document doc-db {:_id "11"})
(def test-doc (get-document doc-db "10"))


     (map (juxt :key :value))
(into {}))

(def foo [9 18 1 3 20 2])

(inc (first (sort > foo)))
(inc (first (sort > (map #(-> % :id str->int ) (all-documents doc-db)))))

(expect true (= 1 (:id (get-landing-sites doc-db test-user)))




  