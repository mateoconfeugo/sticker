(ns causal-marketing.test.handler
  (:use clojure.test
        ring.mock.request  
        causal-marketing.handler))

(defn bingo [one]
  "blah" (println "bingo was his nameo"))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

(bingo 1)



