(ns handler
  (:use [clojure.test :only (is testing deftest)]
        [ring.mock.request]
        [landing-site.handler :only(app)]
        [landing-site.controllers.ad-network-traffic]
        [cheshire.core :only (parse-string)]        
        [expectations]))

(def test-request {:request-method :get
                   :uri "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1}})

(expect true (= (:status (app (request :get (:uri test-request)))) 200))
(expect true (= (:status (app (request :get "/invalid-bogus-fake"))) 404))
(expect true (= (:status (app (request :get "/clientconfig"))) 200))

(def test-client-cfg (parse-string (slurp (:body (app (request :get "/clientconfig")))) true))
(expect true (= (-> test-client-cfg :clientconfig :host_element) "stage"))
