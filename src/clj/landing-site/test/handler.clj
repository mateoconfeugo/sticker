(comment
(ns handler
  (:use [clojure.test :only (is testing deftest)]
        [ring.mock.request]
        [landing-site.handler :only(app)]
        [landing-site.controllers.ad-network-traffic]
        [expectations]))

(def test-request {:request-method :get
                   :uri "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1}})

(def test-response {:status 200
                    :headers {"Content-Type" "text/html"}
                    :body "<h1>Hello user 1</h1>"})

;;test-request
;;test-response

(deftest test-adnetwork-traffic-view
  (testing "extracting the data from the path"
    (let [response (view test-request)]
      (println response)
      (is (= (:status response 200))))))

(test-adnetwork-traffic-view)

(view test-request)



(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

(test-app)
)