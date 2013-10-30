(ns payment-gateway.controllers.api
  ^{:author "Matthew Burns"
    :doc "API controller for management client brings together client user data, database"}
  (:require [com.ashafa.clutch :refer [couch create! document-exists? get-document put-document with-db]]
            [taoensso.timbre :refer [info]]
            [flourish-common.models.orm-spec :refer [mgmt-dbh user]]
            [flourish-common.authoring-utils :refer [new-uuid]]            
            [site-builder-udc.config :refer [config read-config]]
            [clojure.java.jdbc :as jdbc :refer [with-query-results with-connection]]
            [clojure.java.jdbc.sql :as sql :refer [select where]]
            [payment-gateway.credit-card.processor  :refer [add-credit-card-to-user-account]]
            [site-builder-udc.site-builder :as sb :refer [update-landing-site save-landing-site  get-new-landing-site-id]]))

(defn add-credit-card
  [{:keys [first-name last-name company street-address-1 street-address-2 city
           state-province postal-code country phone credit-card-number expiration-date
           card-contact-email special-notes user-id account-id] :as credit-card-data}]
  "Persist the user authoring edits"  
  (let [cfg (read-config)
        _ (info (dissoc credit-card-data :credit-card-number))
        mysql-db {:subprotocol "mysql"
                  :subname (str "//" (:mgmt-db-host cfg) ":" (:mgmt-db-port cfg) "/" (:mgmt-db-name cfg))
                  :user (:mgmt-db-user cfg)
                  :password (:mgmt-db-password cfg)}
        user (first (jdbc/with-connection mysql-db
                      (jdbc/with-query-results res ["select * from user where id =?" user-id]
                        (doall res))))]
    (add-user-credit-card-to-account {:user user :credit-card credit-card-data :account account-id})))
