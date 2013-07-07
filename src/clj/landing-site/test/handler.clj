(ns handler
  (:require [clojure.java.io :as io])
  (:use [clojure.test :only (is testing deftest)]
        [clojurewerkz.urly.core :only [host-of]]                
        [ring.mock.request]
        [cms.site]
        [landing-site.handler]
        [landing-site.config]
        [landing-site.controllers.ad-network-traffic]
        [landing-site.views.host-dom]
        [cheshire.core :only (parse-string parse-stream)]        
        [expectations]))


(def foo (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))
(def base-dir (str website-dir "/patientcomfortreferral.com/site"))
(:header_image (parse-string (slurp (str  base-dir  "/landing_site/1/1.json")) true))
(get-header-image foo)
(cms-header-image base-dir 1)
(get-fonts foo)
(def path (str website-dir "/patientcomfortreferral.com/site/landing_site/1/1.json"))
(def rdr (io/reader path))
(println (.readLine rdr))
(def ham (parse-stream (io/reader path) true))
(map :font_href (:font_link(first (:fonts ham))))

(def ham (slurp path))
(slurp 
(defn get-market-vector-x
  [domain-name website-dir  matrix-id]
  (:market_vector_id (first (parse-string (slurp (str website-dir "/" domain-name "/site/market_matrix/" matrix-id "/" matrix-id ".json")) true))))
  
(defn cms-client-x
  ([req website-dir & market-vector-id]
  (let [domain (if-let [domain-name (-> req :params :ls-url)] domain-name (str (host-of (:server-name req)) "/"))
        matrix-id (or (-> req :params :matrix-id) 1)
        mv-id (or market-vector-id (or (-> req :params :market-vector) (get-market-vector domain website-dir matrix-id)))]
    (new-cms-site {:webdir  website-dir :domain-name domain :market-vector-id mv-id}))))


;;                   :uri "http://patientcomfortreferral.com/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view"
(def test-request {:request-method :get
                   :uri "/?ls-url=patientcomfortreferral.com&market_vector=1"
                   :server-name "http://patientcomfortreferral.com"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1 :ls-url "patientcomfortreferral.com"}})
                           
(comment
(if-let [domain-name (-> test-request :params :ls-url)] domain-name (str (host-of (:server-name test-request)) "/"))
(get-market-vector-x "patientcomfortreferral.com" website-dir 1)

(get-site-id  (str  website-dir "/patientcomfortreferral.com/site") 1)
(def x-client (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))

(show-settings x-client)
(get-site-contents x-client)
(landing-site.views.host-dom/render x-client)
(type x-client)


(def altered-req (testy test-request))
(show-settings (:cms altered-req))
(get-fonts (:cms altered-req))

(expect true (= (:status (app (request :get "/?ls-url=patientcomfortreferral&market_vector=1"))) 200))
(expect true (= (:status (app (request :get "/invalid-bogus-fake"))) 404))
(expect true (= (:status (app (request :get "/clientconfig"))) 200))

(def test-client-cfg (parse-string (slurp (:body (app (request :get "/clientconfig")))) true))
(expect true (= (-> test-client-cfg :clientconfig :host_element) "stage"))
)