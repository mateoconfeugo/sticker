(ns delivery-engine.partner.conf.1086
  (:use [delivery-engine.partner-listings-transform]))

(defn create-1086 []
  (reify PartnerListingTransform
    ;;------------------------------------------------------
    ;;------------------------------------------------------
    (create-uri [this query ctx req actor subid]
      (let [base-uri "http://xml.genieknowsinc.com/feed?"
            feed-id 1086
            sid 18144
            auth "U8oK"
            options {:sid sid
                     :auth auth
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
        (str base-uri (encode-params options))))
    
    (tag-map [this] {:title "title"
                     :description "description"
                     :display-uri "site_url"
                     :uri "mp_redirect_url"
                     :cpc  "mp_bid"
                     })
    (tag [this]
      "name of the feed"
      (str "GenieKnows_Global"))

    (xml-path [this]
      "heirarchy"
      (str "feed.results.sponsored.listing"))

