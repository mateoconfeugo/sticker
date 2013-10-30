(ns management.models.cms-config-file
  (:require [clojure.java.jdbc :as sql :refer [with-connection with-query-results]]))


(defn all-sites [db-conn]
  "sequence of all the sites a user has access to"    
  (sql/with-connection db-conn
    
    (sql/with-query-results results
      ["select * from user order by id desc"]
      (into [] results))))

(comment
(defn create-site [user-settings xml-file]
  "creates a website in the cms for editing
   using pallet logs into the cms server transfers the
   xml config file and then remotely run the sitebuilder
   create command using the config file as input"))
