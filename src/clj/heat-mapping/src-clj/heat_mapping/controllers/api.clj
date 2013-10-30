(ns heat-mapping.controllers.api
  "Contains the functionality to handle landing-site streams of coordinates or report on activity"
  (:require [cheshire.core :refer [generate-string]]
            [clj-logging-config.log4j :as log-config :refer [set-logger!]]        
            [clojure.tools.logging :as log :refer [info]]
            [heat-mapping.config :refer [config]]
            [ring.util.response :refer [content-type response redirect]]))

(log-config/set-logger! :level :debug
                        :out (org.apache.log4j.FileAppender.
                              (org.apache.log4j.EnhancedPatternLayout. org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
                              (config :heat-mapping-log-path) true))

(defn fetch-map [landing-site-id] {:data "blah"})

(defn handle-coord  [post-data]
  "accept a heat map x y coordinate from client"
  (let [coord (assoc post-data :x (:x post-data) :y (:x post-data))
        json (generate-string coord)]
    (if (:x coord)
      (do
        (log/info json)
        (-> (response json) (content-type "application/json")))
      {:status 500 :headers {} :body (generate-string coord)})))


