(ns delivery-engine.core
  (:require [redis.core :as redis])  
  (:refer-clojure :exclude (resultset-seq))
  (:require [clojure.java.jdbc :as sql])
  (:import (java.io ByteArrayInputStream))
  (:require [clojure.xml :as xml])
  (:require [clojure.zip :as z])
  (:require [cheshire.core :as json]
            [cheshire.generate :as gen]
            [cheshire.factory :as fact]
            [cheshire.parse :as parse])  
  (:require [http.async.client :as http]))

;;(defn partner-feed-uri-list [partner-db token keyword]
(defn partner-feed-uri-list []
  "create the query uris for the partners associated with this token"
  ["http://localhost:5000/delivery" "http://localhost:5000/delivery"])                

(defn get-keyword-bid [ads-db keyword]
  "create the query uris for the partners associated with this token")

(defn apply-request-filters [request]
  "Determines the data format to send listing with"
  request)

(defn extract-query [request]
  "Extracts the query from munged uri"
  "shoes")

(defn extract-token [request]
  "Extracts the token from munged uri"
  "red basketball shoes")

(defn select-format [token]
  "Determines the data format to send listing with")

(defn record-impressions [listings query token actor role]
  "Saves a record of these impressions being served")


(defn apply-listing-filter [listings token query actor role]
  "Determines the data format to send listing with"
  listings)

(defn get-filter-policy [request token actor role]
  "Creates a composition filter function to remove listings based on request type, rules tied to the token actor and role"
  (let [request-filter-fn ()
        token-filter-fn ()
        actor-filter-fn ()
        role-filter-fn ()]
    (comp request-filter-fn token-filter-fn actor-filter-fn role-filter-fn)))


(defn get-keyword [query]
  "Extrapolates what keyword to used based on the query"
  "shoes")

(defn biz-actor [token]
  "The business entity requesting listings"
  "traffic partner" )

(defn biz-role [token]
  "The role the entity requesting listing is playing"
  "publishing")

(defn select-format [token]
  "Determines the data format to send listing with"
  "json")

(defn render [listings format]
  "converts the listing into the right data format for the client"
  (json/encode listings))

(defn request-listings  [partner-uris]
  "Concurrently download the listing responses"
  (let [listings (ref [])]
  (doseq [uri partner-uris]
     (with-open [client (http/create-client)]
       (let [response (future (http/GET client uri))]
         (dosync
          (ref-set listings (conj @listings (http/body @response)))))))
  (do @listings)))

(defn gather-database-listings [ads-db keyword]
  "get ad from local network if there is coverage"
  [])
;;          (sql/with-connection ads-db
;;            (sql/do-commands (str "SELECT *  FROM ads WHERE keyword=" keyword))))

(defn remove-below [min-bid listings]
  "get rid of listings that did not exceed minimum bid"
  (filter (fn [x] (>= (Double/parseDouble (:bid x)) min-bid)) listings))

(defn get-struct-map [xml]
  (if-not (empty? xml)
    (let [stream (ByteArrayInputStream. (.getBytes (.trim xml)))]
      (xml/parse stream))))

(defn transform-partner-listings [xml translation-map]
 (let [raw (map #((get-struct-map (str %)) :content) xml)]
   (map #(:attrs %) (flatten raw))))
