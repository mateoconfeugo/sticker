(ns delivery-engine.test.core
  (:use [delivery-engine.core])
  (:use [clojure.test])
  (:import (java.io ByteArrayInputStream))
  (:require [clojure.xml :as xml])
  (:require [clojure.zip :as zip])
  (:require [clojure.contrib.zip-filter.xml :as zf]))

(def partners [ {:id 1 :uri "http://localhost:5000/delivery"}
                {:id 4 :uri "http://localhost:5000/delivery"}                
                ])

(deftest parallel-download
  (let [partner-listings (request-listings (map :uri partners))
        listings (transform-partner-listings l (fn [] ()))]
    (is (= (count listings) 20) "Incorrect number of feed partner responsed with listings.")))
