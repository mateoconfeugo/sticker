(ns delivery-engine.core
  (:require [redis.core :as redis])  
  (:refer-clojure :exclude (resultset-seq))
  (:require [clojure.java.jdbc :as sql])
  (:require [clojure.zip :as z])
  (:require [ring.middleware.params :as ring])
  (:require [cheshire.core :as json]
            [cheshire.generate :as gen]
            [cheshire.factory :as fact]
            [cheshire.parse :as parse])
  (:require [http.async.client :as http]))

;;--------------------------------------------------------------------------
(defn remove-below [min-bid listings]
  "get rid of listings that did not exceed minimum bid"
  (filter (fn [x] (>= (Double/parseDouble (:bid x)) min-bid)) listings))

;;--------------------------------------------------------------------------
(defn auction [listings minimum-bid]
      "Determine which ads get selected and in which order"
      (sort-by :bid (remove-below minimum-bid (distinct listings ))))

;;--------------------------------------------------------------------------
(defn get-keyword [query]
  "Extrapolates what keyword to used based on the query"
  "shoes")

;;--------------------------------------------------------------------------
(defn get-keyword-bid [ads-db keyword]
  "create the query uris for the partners associated with this token")

;;--------------------------------------------------------------------------
(defn apply-request-filters [request]
  "Determines the data format to send listing with"
  request)

;;--------------------------------------------------------------------------
(defn apply-listing-filter [listings token query actor role]
  "Determines the data format to send listing with"
  listings)

;;--------------------------------------------------------------------------
(defn get-filter-policy [request token actor role]
  "Creates a composition filter function to remove listings based on request type, rules tied to the token actor and role"
  (let [request-filter-fn ()
        token-filter-fn ()
        actor-filter-fn ()
        role-filter-fn ()]
    (comp request-filter-fn token-filter-fn actor-filter-fn role-filter-fn)))

;;--------------------------------------------------------------------------
(defn extract-query 
  "Extracts the query from munged uri"
  [request]
  ((#'ring/parse-params (request :query-string) :query)))

;;--------------------------------------------------------------------------
(defn extract-token 
  "Extracts the token from munged uri"
  [request]
  ((#'ring/parse-params (request :query-string) :token)))  

;;--------------------------------------------------------------------------
(defn select-format [token]
  "Determines the data format to send listing with"
  (str "json"))

;;--------------------------------------------------------------------------
(defn record-impressions [listings query token actor role]
  "Saves a record of these impressions being served")

;;--------------------------------------------------------------------------
(defn biz-actor [token]
  "The business entity requesting listings"
  "traffic partner" )

;;--------------------------------------------------------------------------
(defn biz-role [token]
  "The role the entity requesting listing is playing"
  "publishing")

;;--------------------------------------------------------------------------
(defn select-format [token]
  "Determines the data format to send listing with"
  "json")

;;--------------------------------------------------------------------------
(defn render [listings format]
  "converts the listing into the right data format for the client"
  (json/encode listings))

;;--------------------------------------------------------------------------
(defn request-listings  [partner-uris]
  "Concurrently download the listing responses"
  (let [listings (ref {})]
  (doseq [p partner-uris]
     (with-open [client (http/create-client)]
       (let [response (future (http/GET client (p :uri)))]
         (dosync
          (ref-set listings (assoc @listings (p :feed-id) (http/body @response)))))))
  (do @listings)))

;;--------------------------------------------------------------------------
(defn gather-database-listings [ads-db keyword]
  "get ad from local network if there is coverage"
  [])
;;          (sql/with-connection ads-db
;;            (sql/do-commands (str "SELECT *  FROM ads WHERE keyword=" keyword))))



(comment
(defn build-keyword-categorization-hierarchy
  {keywords {}
   categories {
               :canonical { :keywords ["foo" "bar"] }
               }
   })


(defn map-query-string-to-category
  "Takes a phrase of text and returns the taxanomy categorization of the data"
  [query])

(defn select-keyword-from-query-and-categorization
  "Figure out a query to use based on how the query search vector and a categorization bucket list")


(defn contextualize
  "Scrape the the content at the uri and create a category"
  [uri bias])
)