(ns test.controllers.ad_network_traffic
  (:use [clojure.test :only (is testing deftest)]
        [compojure.core]
        [ring.mock.request]
        [clout.core]
        [flourish-common.web-page-utils]
        [landing-site.handler :only(app)]
        [landing-site.controllers.ad-network-traffic]))

(def test-request {:request-method :get
                   :uri "/img/44af7dcf.png"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1 :ls-url "patientcomfortreferral.com"}})

(render-to-response (routing test-request landing-site-routes))

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
