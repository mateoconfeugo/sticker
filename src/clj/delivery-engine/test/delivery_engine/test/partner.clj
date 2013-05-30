(ns delivery-engine.test.partner
  (:use [delivery-engine.partner-listings-transform])
  (:require [clojure.java.jdbc :as sql])  
  (:use [clojure.test]))

(def test-databases
  (if-let [dbs (System/getenv "TEST_DBS")]
    (map keyword (.split dbs ","))
    ;; enable more by default once the build server is equipped?
    [:mysql :sqlite]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "root"
               :password ""})

(def sqlite-db {:subprotocol "sqlite"
                :subname "clojure_test_sqlite"})

(def mysql-str-db
  "mysql://clojure_test:clojure_test@localhost:3306/clojure_test")

(def mysql-jdbc-str-db
  "jdbc:mysql://clojure_test:clojure_test@localhost:3306/clojure_test")

(defn- test-specs
  "Return a sequence of db-spec maps that should be used for tests"
  []
  (for [db test-databases]
    @(ns-resolve 'clojure.java.test-jdbc (symbol (str (name db) "-db")))))

(defn- clean-up
  [database]
  (sql/with-connection database
    (doseq [table [:feed_partner]]
      (try
        (sql/drop-table table)
        (catch Exception _
          ;; ignore
          )))))

(use-fixtures
 :each clean-up)

(def db mysql-db)

(deftest test-create-feed-partner-table 
  (create-feed-partner-table db)
  (sql/with-connection db
    (is (= 0 (sql/with-query-results res ["SELECT * FROM feed_partner"] (count res)))))
  (drop-table "feed-partner" db))

(deftest test-drop-feed-partner-table
  (create-feed-partner-table db)
  (sql/with-connection db
    (try
      (sql/drop-table :feed_partner)
      (catch Exception _))
    (is (thrown? java.sql.SQLException (sql/with-query-results res ["SELECT * FROM feed_partner"] (count res)))))
  )

(deftest test-insert-rows-into-feed-partner
  (create-feed-partners db)  
  (sql/with-connection db
    (try 
      (let [r (sql/insert-rows
               "feed_partner"
               [1 "genie_knows" ]
               [2 "google" ])
        (is (= '(1 1) r)))
      (catch Exception _))
    (is (= 2 (sql/with-query-results res ["SELECT * FROM feed_partner"] (count res))))
    (is (= "google" (sql/with-query-results res ["SELECT * FROM feed_partner WHERE tag_name = ?" "google"] (:tag_name (first res)))))
    (is (= "genie_knows" (sql/with-query-results res ["SELECT * FROM feed_partner WHERE tag_name = ?" "genie_knows"] (:tag_name (first res))))))
;;  (drop-table "feed-partner" db)
  )

(defn feed-table-insert-rows [dsn]
  (sql/with-connection dsn
    (try 
      (sql/insert-rows
       "feed"
       [1  1 "genie_knows" "http://localhost:5000/delivery"]
       [2  2 "google" "http://localhost:5000/delivery"])
        (catch Exception _))))

(deftest test-insert-rows-into-feed
  (create-feed-table db)
  (feed-table-insert-rows db)
  (sql/with-connection db  
    (is (= 2 (sql/with-query-results res ["SELECT * FROM feed"] (count res))))
    (is (= "google" (sql/with-query-results res ["SELECT * FROM feed WHERE tag_name = ?" "google"] (:tag_name (first res)))))
    (is (= "genie_knows" (sql/with-query-results res ["SELECT * FROM feed WHERE tag_name = ?" "genie_knows"] (:tag_name (first res))))))
  (drop-table "feed" db))

(deftest get-feed-test
  (create-feed-table db)
  (feed-table-insert-rows db)
  (let [p (get-feed 1 db)]
    (is (= (p :feed-id) 1) "Unable to retreive feed partner"))
  (drop-table "feed" db))

(comment
  (deftest get-struct-map
    (let [partner-listings (request-listings (map :uri partners))
          listings (transform-partner-listings partner-listings {})]
      (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))


  (deftest xml-to-data
    (let [partner-listings (request-listings (map :uri partners))
          listings (transform-partner-listings partner-listings {})]
      (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

  (deftest load-feed-partner
    (let [partner-listings (request-listings (map :uri partners))
          listings (transform-partner-listings partner-listings {})]
      (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))



  (deftest standard-context
    (let [partner-listings (request-listings (map :uri partners))
          listings (transform-partner-listings partner-listings {})]
      (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

  (deftest generate-query-uri
    (let [partner-listings (request-listings (map :uri partners))
          listings (transform-partner-listings partner-listings {})]
      (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))
  )