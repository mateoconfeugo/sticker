(ns site-builder-udc.db
  "Functions related to manipulating the persistance store for the
   site builders authoring artifacts/resources"
  (:require [clojure.string :as str]
            [clojure.walk :refer [keywordize-keys]]
            [com.ashafa.clutch :as clutch]
            [flourish-common.db :refer [create-user-authoring-database get-user-database]])
  (:import (java.net URI)))

(declare ^:dynamic *cfg*)

(defn get-site-builder-db-host
  []
  (or (System/getenv "SITE_BUILDER_DATABASE_HOST") (:db-host *cfg*)))

(defn get-site-builder-db-port
  []
  (or (System/getenv "SITE_BUILDER_DATABASE_PORT") (:db-port *cfg*)))

(defn get-site-builder-db-username-prefix
  []
  (or (System/getenv "SITE_BUILDER_DATABASE_NAME_PREFIX") (:db-name-prefix *cfg*)))

(defn get-site-builder-db-api-key
  []
  (or (System/getenv "SITE_BUILDER_DATABASE_API_KEY") (:api-key *cfg*)))

(defn get-site-builder-db-api-password
  []
  (or (System/getenv "SITE_BUILDER_DATABASE_API_PASSWORD") (:api-password *cfg*)))

(defn site-builder-resource
  [user]
  "Assemble the database dsn path for a site-builder user
   authoring artifacts and resources"
  (assoc (cemerick.url/url (get-site-builder-db-host) (str (get-site-builder-db-username-prefix ) (:username user)))
    :username (get-site-builder-db-api-key)
    :password (get-site-builder-db-api-password)))

(defn get-landing-sites
  [db user]
  "Get a list of all the users landing site"
  (->> (clutch/get-view db (str (get-site-builder-db-username-prefix ) "-" (:username user)) :all)
       (map (juxt :key :value))
       (into {})
       keywordize-keys))

(defn site-builder-db
  [user]
  "Get the users authoring database for site-builder artifacts and resources"
  (clutch/get-database (str (get-site-builder-db-username-prefix) "-" (:username user))))
