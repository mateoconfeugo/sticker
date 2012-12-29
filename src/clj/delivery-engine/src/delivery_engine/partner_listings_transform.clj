(ns delivery-engine.partner-listings-transform
  (:use [delivery-engine.core])
  (:import [java.net URLEncoder])
  (:require [clojure.java.jdbc :as sql])
  (:require [clojure.xml :as xml])  
  (:use [ring.util.codec :only [url-encode]])
  (:import (java.io ByteArrayInputStream))  
  (:require [cupboard.core :as cb]))

(def feed-partners (ref {}));

;;--------------------------------------------------------------------------
(defn standard-context [p r]
  {:ip (r :ip)   })

;;--------------------------------------------------------------------------
(defn transformer-path
  "Get the path to feed partner customization code"
  []
  (get (System/getenv) "FEED_PARTNER_BASE_DIR"))

;;--------------------------------------------------------------------------
(defn get-feed
  "Obtain the partner record"
  [feed-id dsn]
  (sql/with-connection dsn
    (sql/with-query-results rs [(str "SELECT * FROM feed WHERE id=" feed-id)]
      (map (fn[x] ({:feed-id (x :id)  :tag-name (x :tag_name) :uri (x :uri) })) (first rs)))))

;;--------------------------------------------------------------------------
(defn load-feed-partner [fid]
  "load customization code for creating uris for listings and standardizing the results get from cache if possible"
  (let [partner (get-feed-partner fid)
        tag (partner :tag-name)]
    (cond
      (contains? feed-partners fid) (feed-partners fid)
      :default  (do
                  (load-file (str (transformer-path) tag))
                  (let [engine ((str "create-" tag))]
                    (ref-set feed-partners (assoc @feed-partners {:feed-id engine}))
                    engine)))))

;;--------------------------------------------------------------------------
(defprotocol PartnerListingTransform
  "the operations used by a parser converting partners listing format into desired format"
  (create-uri [this query ctx]
    "Create the url that the browser will be pointed to upon clicking the ad")
  (tag-map [this]
    "The map of the partner feed listing fields to desired format")
  (feed-tag [this]
    "Human readable name of this feed")
  (xml-path [this]
    "Where in the xml listing tree are the actual listings"))

;;--------------------------------------------------------------------------
(defn- create-feed-ctx-dispatch [partner request]
  (do
    (load-file (str (:name-tag partner) ".clj"))
    (partner :name-tag)));

(defmulti create-feed-ctx
  "create the context that will be used for creating a query uri for the feed partner listings"
  #'create-feed-ctx-dispatch
  :default nil)

(defmethod create-feed-ctx nil
  [partner request]
  (standard-context partner request))

;;--------------------------------------------------------------------------
(defn encode-params [m]
  (->> (for [[k v] m]
         (str (url-encode k) "=" (url-encode v)))
       (interpose "&")
       (apply str)))

;;--------------------------------------------------------------------------
(defn build-uri [uri-base query-map]
  (str uri-base "?" (encode-params query-map)))

;;--------------------------------------------------------------------------
(defn get-struct-map [xml]
  (if-not (empty? xml)
    (let [stream (ByteArrayInputStream. (.getBytes (.trim xml)))]
      (xml/parse stream))))

;;--------------------------------------------------------------------------
(defn xml-to-data [xml]
 (let [raw (map #((get-struct-map (str %)) :content) xml)]
   (map #(:attrs %) (flatten raw))))

;;--------------------------------------------------------------------------
(defn parse-xml [xml tag-map xml-path]
  "Convert partner xml into Listing record")
  ;;  tag-map xml-path]

;;--------------------------------------------------------------------------
(defn parse-listings [listings]
  "The default parser-fn"
  (doseq [fid (keys listings)]
    (let [xml (listings fid)
          partner (memoize (load-feed-partner fid))
          tm (memoize (tag-map partner))
          xp (memoize (xml-path partner))]
      (parse-xml xml tm xp))))

;;--------------------------------------------------------------------------
(defn generate-query-uri 
  "Transform a request to uri query string for obtaining listings from partner"
  [request partner]
  (let [ctx (create-feed-ctx partner request)
        query (extract-query request)
        engine (memoize ("create-" (partner :name-tag) ) )]
    (create-uri engine query ctx)))

;;--------------------------------------------------------------------------
(defn partner-feed-uri-list [request token db]
  "create the query uris for the partners associated with this token"
  (sql/with-connection db
                      (let [table "feed-partner-token"
                            query (str "SELECT uri FROM " table " WHERE token=" token)
                            uri-factory (partial generate-query-uri request)                            
                            partners (map (fn[x] ({:feed-id (x :feed-id) :uri (x :uri) }) (sql/with-query-results rs [query] (doall rs))))]
                        (map  uri-factory partners))))



