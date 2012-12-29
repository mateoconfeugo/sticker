(ns delivery-engine.search
  (:use [delivery-engine.core])
  (:use [delivery-engine.data-api])
  (:use delivery-engine.partner-listings-transform))

(gen-interface
 :name delivery.search.Engine
 :methods [ [showSettings [] void]
            [search [String] String]])

(gen-class
 :name delivery.impl.EngineImpl  :implements [delivery.search.Engine]
 :state state :init init :methods )

(defn -init[])

(def cfg {:db-host "" :db-port "" :db-name "" :db-user "" :db-password ""
          :cache-host "" :cache-part "" :cache-db "" :cache-db-index ""
          :timer-cb "" :clock-period "" :delay ""
          :format "" :network "" :bid-algorithm ""})

(defn -showSettings [this]
  (println cfg))

(defn -listings [this keyword token request]
  (let [uris (partner-feed-uri-list request token)
        minimum-bid (get-keyword-bid keyword)
        listings (parse-listings (request-listings uris))]
    (auction listings minimum-bid)))

(defn -search [this request]
  (let [request (build-request request)
        ;;        request (apply-request-filters request)
        query (extract-query request)
        keyword (get-keyword query)              
        token (extract-token query)
        actor (biz-actor token)
        role (biz-role token)]
    ;;              policy (get-filter-policy request token actor role)
    ;;              listings (policy (gather-listings this keyword token))
    (render (-listings this keyword token request) (select-format token))))

(gen-class
 :name delivery.search.EngineFactory
 :methods [^{:static true} [createEngine [] delivery.search.Engine]])
 
(defn -createEngine []
  (delivery.impl.EngineImpl. ))

