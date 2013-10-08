(ns management.models.user
  (:require [clojure.java.jdbc :as sql :refer[with-connection with-query-results insert-values]]
            [clj-time.format :refer [show-formatters formatter unparse]]
            [clj-time.local :refer [local-now]]))

(defn all-users [db-conn]
  (sql/with-connection db-conn
    (sql/with-query-results results
      ["select * from user order by id desc"]
      (into [] results))))

(defn create
  "Add a new user to the user table noting the time of creation and setting their status to new"
  [user db-conn]
  (sql/with-connection db-conn
    (let [fname (:first-name user)
          lname (:last-name user)
          email (:email user)
          status "new"
          custom-formatter (formatter "yyyy-MM-dd hh:mm:ss")
          created (unparse custom-formatter (local-now))]
      (sql/insert-values :user [:first_name :last_name :email_username :status :created] [fname lname email status created]))))
