(comment
(ns test.controllers.ad_network_traffic
  (:use [clojure.test :only (is testing deftest)]
        [ring.mock.request]
        [clout.core]
        [landing-site.handler :only(app)]
        [landing-site.controllers.ad-network-traffic]))

(def test-request {:request-method :get
                   :uri "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1}})

(deftest view-test
  (testing "extracting the data from the path"
    (let [response (view test-request)]
      (do
      (println response)
      (is (= (:status response 200)))))))

(route-matches "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view"
               (request :get "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view"))

(view-test)
(view test-request)
)