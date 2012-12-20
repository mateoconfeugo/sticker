(ns delivery-engine.partner-listings-transform
  (:use [delivery-engine.core])
  (:require [cupboard.core :as cb]))

(defprotocol PartnerListingTransform
  "the operations used by a parser converting partners listing format into desired format"
  (create-url [this, query, request-ctx, actor]
    "Create the url that the browser will be pointed to upon clicking the ad")
  (tag-map [this]
    "The map of the partner feed listing fields to desired format")
  (feed-tag [this]
    "Human readable name of this feed")
  (translate [this listing template format]
    "parses partners ad format into desired formate for use in system")
  (select-translation-map [this token]
    "the code unique to each feed partner that maps listings in common desired data structure")
  )

(defn new-listing-translator [cache-spec]
  (reify PartnerListingTransform
    (create-url [this query request-ctx actor]      )
    (tag-map [this])
    (feed-tag [this])

    ;; get from cache of file of clojure
    (select-translation-map [this token]
      {})
;;      (let [db-index (:db cache-spec)
;;            translation (cupboard.bdb.je/db-get db-index token)]))
      
    (translate [this listing translation-map format]
      "parses partners ad format into desired formate for use in system"
      (render (translation-map listing) format))))