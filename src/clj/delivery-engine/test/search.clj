(ns delivery-engine.test.search
  (:use delivery-engine.config)
  (:use delivery-engine.core)  
  (:use delivery-engine.search)
  (:use  [delivery-engine.data-api])
  (:use [clojure.test])
  (:import java.io.File)
;;  (:use ring.util.request ring.util.io)
  ;;  (:import [delivery-engine.data-api Request Response Listing Budget-Item Usage-Summary Node-Usage-Summary Cluster-Usage-Summary]))
  )

(def cfg  (-> (config) :delivery-engine))
(def test-settings (assoc cfg :clock-period 100 :delay 1000
                          :cache-host "10.0.147.102" :cache-db-index (:cache-db-index cfg) :cache-port 6379))

(def publisher-request-url {:scheme :http
                            :uri "/delivery/feed"
                            :headers {"host" "florish.com"}
                            :query-string "token=1212&query=shoes"})

(def query {:keyword "shoes"})
(def partner-request-1 (map->Listing {:feed-id 8087 :level-id 1 :level "source" :count count :query query}))
;; (def expected-listings [listing-1 listing-2 listing-3 listing-4])

(def expected-listings [])

(def test-cookies {:user 1 :network 1 :tracking 1 :mode 1 :request-tuple partner-request-1})

(def publisher-request (map->Request {:token 1212
                                              :level-id 1
                                              :level "publisher"
                                              :query "shoes"
                                              :agent "test-agent"
                                              :referring-url ""
                                              :cookies test-cookies}))

;; Assemble a delivery engine workflow out of the pieces
(defn test-delivery-engine
  [http-request]
  (let [query (:query http-request)
        token (:token query)
        actor (biz-actor token)
        role (biz-role token)        
        engine (new-delivery-engine test-settings)                
        listings (search engine http-request)]
    (record-impressions listings query token actor role)))

;; Top level component acceptance test
(deftest search-engine-test
    (is (= expected-listings (test-delivery-engine publisher-request-url))))

(comment
(deftest gather-partner-listings-test  
  (let [engine (new-delivery-engine test-settings)
        listings-returned (gather-partner-listings engine request-1)]
    (is (= test-listings listings-returned))))

(deftest gather-database-listings-test  
  (let [engine (new-delivery-engine test-settings)
        listings-returned (gather-database-listings engine request-1)]
    (is (= test-listings listings-returned))))

(deftest translate-partner-listings-test  
  (let [engine (new-delivery-engine test-settings)
        listings-returned (translate-partner-listings engine request-1)]
    (is (= test-listings listings-returned))))

(deftest gather-listing-test  
  (let [engine (new-delivery-engine test-settings)
        listings-returned (gather-listings engine request-1)]
    (is (= test-listings listings-returned))))
)




    
      
       
