(ns flourish-common.db
  (:require [clojure.string :as str]
            [com.ashafa.clutch :as clutch]
            [clojure.java.jdbc :as sql :refer [with-connection create-table]])
  (:import (java.net URI)))

(def tables ["user profile"])

(defn drop-database
  [db-spec name]
  (sql/with-connection db-spec
    (with-open [s (.createStatement (sql/connection))]
      (.addBatch s (str "DROP DATABASE IF EXISTS " name))
      (seq (.executeBatch s)))))

(defn create-database
  [db-spec name]
  (sql/with-connection db-spec
    (with-open [s (.createStatement (sql/connection))]
      (.addBatch s (str "DROP DATABASE IF EXISTS " name))
      (.addBatch s (str "create database " name))
      (seq (.executeBatch s)))))

(defn drop-table [table dsn]
  (sql/with-connection dsn
    (try
      (sql/drop-table (keyword table))
      (catch Exception _))))

(defn drop-tables
  "remove the database the has all the feed partner data"
  [dsn tables] (doseq [t tables] (drop-table t dsn)))

(defn create-user-authoring-database
  [prefix username]
  "Create the logical persistance store for the user authored artifacts"
  (clutch/create! (clutch/couch (str prefix "-" username ))))

(defn get-user-database
  [prefix username]
  "Retreve the logical persistance store for the user authored artifacts"
  (clutch/create! (clutch/couch  (str prefix"-" username))))

(defn map-from-db
  "Turn the CouchDB map into the CMS map"
  [db-map]
  (if-let [data (:data db-map)]
    (assoc (dissoc data :type) :id (:_id db-map))))

(defn initialize-management-database
  [db-spec]
  "Build schema for the management database"
  (sql/with-connection db-spec
    (sql/create-table  :user
                       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                       [:username "VARCHAR(50) NOT NULL"]
                       [:password "VARCHAR(225) NOT NULL"])
    (sql/create-table  :profile
                       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                       [:user_id "INTEGER(10) NOT NULL"]
                       [:tag "VARCHAR(225) NOT NULL"]
                       [:query_uri "TEXT NOT NULL"])))
