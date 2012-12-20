(ns delivery-engine.test.async-http-client
  (:use [clojure.test])  
  (:use delivery-engine.config)
  (:require [http.async.client :as http]))

(def cfg  (-> (config) :delivery-engine))

(def partners [ {:id 1 :url "http://www.google.com"}
                {:id 2 :url "http://www.yahoo.com"}
                {:id 3 :url "http://www.bing.com"}])

(comment
                {:id 4 :url "http://www.valueclick.com"}
                {:id 5 :url "http://www.reachlocal.com"}
                {:id 6 :url "http://www.genieknows.com"}
                {:id 7 :url "http://www.miva.com"}
                {:id 8 :url "http://www.mirago.com"}
)

(deftest async-http-get

  )

(defn long-calculation [num1 num2]
  (Thread/sleep 5000)
  (* num1 num2))

(defn long-run []
  (let [x (long-calculation 11 13)
        y (long-calculation 10 13)
        z (long-calculation 1 13)]
    (* x y z)))

(defn fast-run []
  (let [x (future (long-calculation 11 13))
        y (future (long-calculation 10 13))
        z (future (long-calculation 1 13))]
    (* @x @y @z)))



(defn bulk-fetch
  [partners]
  (doseq [p partners]
    (println (:url p))
     (with-open [client (http/create-client)]
       (let [response (future (http/GET client (:url p)))]
         (println (http/body @response))))))

;;(-> response
;;             http/await
;;             http/string)))))







