(ns delivery-engine.test.core
  (:use [delivery-engine.core])
  (:use [clojure.test])
  (:import (java.io ByteArrayInputStream))
  (:require [clojure.xml :as xml])
  (:require [clojure.zip :as zip])
  (:require [clojure.contrib.zip-filter.xml :as zf]))

(def partners [ {:feed-id 1 :src-id 1 :uri "http://localhost:5000/delivery"}
                {:feed-id 2 :src-id 4 :uri "http://localhost:5000/delivery"}                
                ])

(deftest listings-retrieval
    (is (= (count (request-listings partners)) 2) "Incorrect number of feed partner responsed with listings."))


(comment
(deftest remove-below
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))


(deftest transformer-path 
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest partner-feed-uri-list
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))


(deftest get-keyword-bid
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest apply-request-filters 
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest extract-query
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest extract-token
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest select-format
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest record-impressions
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest apply-listing-filter
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest get-filter-policy
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest get-keyword
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest biz-actor
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest biz-role
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest select-format
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

(deftest render
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings partner-listings {})]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))

)





















