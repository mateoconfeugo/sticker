(ns delivery-engine.partner.conf.genie-knows
  (:use [delivery-engine.partner-listings-transform]))

(defmethod create-feed-ctx :genie_knows [partner request]
  (let (ctx {})))

(defn create-genie-knows
  (reify PartnerListingTransform

    ;;------------------------------------------------------
    (create-uri [this query ctx]
      (let [base-uri "http://xml.genieknowsinc.com/feed"
            query-map {:sid (ctx :sid)
                     :auth (ctx :auth)
                     :q query
                     :subid (str subid "_" (ctx :channel-code))
                     :client_id (get-client-id feed-id)
                     :ip (req :ip)
                     :ua (req :ua)
                     :ref (req :ref)
                     :count (ctx :listing-count)
                     :city (ctx :city)
                     :state (ctx :state )
                     :sites (ctx :sites)
                     :adult (adult? feed-id)}]
        (build-uri base-uri query-map)))

    ;;------------------------------------------------------    
    (tag-map [this] {:title "title"
                     :description "description"
                     :display-uri "site_url"
                     :uri "mp_redirect_url"
                     :cpc  "mp_bid"
                     })

    ;;------------------------------------------------------    
    (tag [this]
      "name of the feed"
      (str "GenieKnows_Global"))

    ;;------------------------------------------------------    
    (xml-path [this]
      "Where in heirarchy the listings are"
      (str "feed.results.sponsored.listing"))))

