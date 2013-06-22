(ns handler
  (:use [clojure.test :only (is testing deftest)]
        [clojurewerkz.urly.core :only [host-of]]                
        [ring.mock.request]
        [cms.site]
        [landing-site.handler]
        [landing-site.config]
        [landing-site.controllers.ad-network-traffic]
        [landing-site.views.host-dom]
        [cheshire.core :only (parse-string)]        
        [expectations]))

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
                   :uri "/"
                   :server-name "http://patientcomfortreferral.com"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1
                           }})
(comment
(if-let [domain-name (-> test-request :params :ls-url)] domain-name (str (host-of (:server-name test-request)) "/"))
(get-market-vector-x "patientcomfortreferral.com" website-dir 1)

(get-site-id  (str  website-dir "/patientcomfortreferral.com/site") 1)
(def x-client (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))

(show-settings x-client)
(get-site-contents x-client)
(landing-site.views.host-dom/render x-client)
(type x-client)



(expect true (= (:status (app (request :get (:uri test-request)))) 200))
(expect true (= (:status (app (request :get "/invalid-bogus-fake"))) 404))
(expect true (= (:status (app (request :get "/clientconfig"))) 200))

(def test-client-cfg (parse-string (slurp (:body (app (request :get "/clientconfig")))) true))
(expect true (= (-> test-client-cfg :clientconfig :host_element) "stage"))
)