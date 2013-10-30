(ns landing-site.controllers.heatmap
  (:require [cheshire.core :only [parse-string generate-string]]
        [clj-logging-config.log4j :as log-config]        
        [clojure.tools.logging :as log]
        [compojure.core :refer [defroutes POST]]
        [landing-site.config :refer [cfg heatmap-path] :as app]
        [ring.util.response :refer [content-type response redirect]]))

(log-config/set-logger! :level :debug
                        :out (org.apache.log4j.FileAppender.
                              (org.apache.log4j.EnhancedPatternLayout. org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
                              "logs/heatmap.log" true))

(defn handle-coord
  [post-data]
  "format lead data, store it, send you email, redirect to conversion page"
  (let [coord (assoc post-data :x (:x post-data) :y (:x post-data))
        json (generate-string coord)]
    (if (:x coord)
      (do
        (log/info json)
        (-> (response json) (content-type "application/json")))
      {:status 500 :headers {} :body (generate-string coord)})))
  
(defroutes heatmap-routes
  (POST "/heatmap" {body :body} (handle-coord (parse-string (slurp body) true))))
