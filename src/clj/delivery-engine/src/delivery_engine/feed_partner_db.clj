(ns delivery-engine.feed-partner-db
  (:require [clojure.java.jdbc :as sql]))

(defn drop-table [table dsn]
  (sql/with-connection dsn  
    (try
      (sql/drop-table (keyword table))
      (catch Exception _))))

(defn create-feed-partner-table [dsn]
  (sql/with-connection dsn
    (try
      (sql/create-table
       :feed_partner
       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
       [:tag_name "varchar(25)"])
       (catch Exception _))))

(defn create-feed-table [dsn]
  (sql/with-connection dsn
    (try
      (sql/create-table
       :feed
       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
       [:partner_id "varchar(25)"]
       [:tag_name "varchar(32)"]       
       [:query_uri "varchar(255)"])
      (catch Exception _))))


(defn drop_feed_partners_database
  "remove the database the has all the feed partner data"
  [dsn]
  (let [tables [""]]
    (doseq [t tables]
      (drop-table t dsn))))