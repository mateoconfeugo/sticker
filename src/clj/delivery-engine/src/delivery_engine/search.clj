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

(defn -listings [this keyword token]
  (let [partner-uris     (partner-feed-uri-list )
        partner-xml      (request-listings partner-uris)
        partner-listings (transform-partner-listings partner-xml {})]
    (sort-by :bid (remove-below 0.2 (distinct partner-listings )))))

(comment
  ;;              minimum-bid     0.2    
  ;;partner-uris     (future (partner-feed-uri-list partner-db token keyword))
  ;;              minimum-bid      (future (get-keyword-bid ads-db keyword))
  ;;              local-listings   (future (gather-database-listings ads-db keyword))
  ;;              translation-map  (future (select-translation-map translator token))
  ;;              partner-xml      (future (request-listings @partner-uris))              
  ;;              partner-listings (future (transform-partner-listings @partner-xml @translation-map))]
  ;;          (dosync (sort-by :bid (remove-below @minimum-bid (distinct (merge @partner-listings @local-listings)))))
  )


(defn -search [this request]
  (let [request (apply-request-filters request)
              query (extract-query request)
              keyword (get-keyword query)              
              token 1212
              actor (biz-actor token)
              role (biz-role token)]
              ;;              policy (get-filter-policy request token actor role)
              ;;              listings (policy (gather-listings this keyword token))
    (render (-listings this keyword token) (select-format token))))

(gen-class
 :name delivery.search.EngineFactory
 :methods [^{:static true} [createEngine [] delivery.search.Engine]])
 
(defn -createEngine []
  (delivery.impl.EngineImpl. ))
