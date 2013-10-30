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
  [prefix username sb-db-host db-api-key db-api-password]
  "Assemble the database dsn path for a site-builder user
   authoring artifacts and resources"
  (assoc (cemerick.url/url sb-db-host (format "%s-%s" prefix username))
    :username db-api-key :password db-api-password))

(defn get-landing-sites
  [db prefix username]
  "Get a list of all the users landing site"
  (->> (clutch/get-view db (format "%s-%s" prefix username) :all)
       (map (juxt :key :value))
       (into {})
       keywordize-keys))

(defn site-builder-db
  [prefix user]
  "Get the users authoring database for site-builder artifacts and resources"
  (clutch/get-database (format "%s-%s" prefix user)))
