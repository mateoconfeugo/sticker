(ns site-builder-udc.controllers.api
  ^{:author "Matthew Burns"
    :doc "API controller for site-builder client brings together client user data, database"}
  (:require [com.ashafa.clutch :refer [couch create! document-exists? get-document put-document with-db]]
            [taoensso.timbre :refer [info]]
            [flourish-common.models.orm-spec :refer [mgmt-dbh user]]
            [flourish-common.authoring-utils :refer [new-uuid]]
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]
            [site-builder-udc.db :refer [site-builder-db]]
            [site-builder-udc.site-builder :as sb :refer [update-landing-site save-landing-site  get-new-landing-site-id]]))

(declare ^:dynamic *cfg*)

(comment "What different types of destinations are available as publishinger targets?
  1) heroku
  2) delivery network
 What information is contained in the destination?
 What is the output of the publishing process
 How is the authoring content integrated with the git mangaged lein project template
")

(defn publish
  "Publish landing site to the destination "
  [{:keys [landing-site-id destination-id doc-db] :as request}]
  (let [landing-site (with-db doc-db (get-document landing-site-id))
        destination (with-db doc-db (get-document destination-id))
;;        mover (get-mover destination)
        ]
    ;;    (publish-to destination landing-site mover)
;;    (publish-to destination landing-site)
    ))

(defn save
  "Persist the user authoring edits"
  [{:keys [page-html landing-site-id user-id] :as request}]
  (let [ _ (info request)
        mysql-db {:subprotocol "mysql"
                  :subname (str "//" (:mgmt-db-host *cfg*) ":" (:mgmt-db-port *cfg*) "/" (:mgmt-db-name *cfg*))
                  :user (:mgmt-db-user *cfg*)
                  :password (:mgmt-db-password *cfg*)}
        user (first (jdbc/with-connection mysql-db
                      (jdbc/with-query-results res ["select * from user where id =?" user-id]
                        (doall res))))
        doc-db (site-builder-db user)
        ls-id (or landing-site-id (sb/get-new-landing-site-id doc-db))
        landing-site (if (and landing-site-id (document-exists? doc-db ls-id))
                       (with-db doc-db (get-document landing-site-id))
                       (with-db doc-db (put-document {:_id (str (get-new-landing-site-id doc-db))})))
        saved-site (sb/save-landing-site {:page-html page-html :landing-site-id ls-id :db doc-db})]
    (:page-html saved-site)))

(defn update
  "Persist the user authoring edits"
  [{:keys [xpath page-html snippet-html landing-site-id user-id uuid] :as request}]
  (let [mysql-db {:subprotocol "mysql"
                  :subname (str "//" (:mgmt-db-host *cfg*) ":" (:mgmt-db-port *cfg*) "/" (:mgmt-db-name *cfg*))
                  :user (:mgmt-db-user *cfg*)
                  :password (:mgmt-db-password *cfg*)}
        user (first (jdbc/with-connection mysql-db
                      (jdbc/with-query-results res ["select * from user where id =?" user-id]
                        (doall res))))
        uuid (or uuid (new-uuid))
        doc-db (site-builder-db user)
        ls-id (or landing-site-id (get-new-landing-site-id doc-db))
        landing-site (if (and landing-site-id (document-exists? doc-db ls-id))
                       (with-db doc-db  (get-document landing-site-id))
                       (with-db doc-db (put-document  {:_id (str (sb/get-new-landing-site-id doc-db))})))
        updated-site (sb/update-landing-site {:page-html page-html :landing-site-id ls-id :db doc-db
                                              :snippet-html snippet-html :uuid uuid :xpath xpath})]
    {:page-html (:tmp-page-html updated-site)
     :uuid uuid
     :xpath xpath
     :landing-site-id landing-site-id
     :user-id user-id}))
