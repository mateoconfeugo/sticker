(ns causal-marketing.handler
  (:use [compojure.core]
        [ring.adapter.jetty :as ring]
        [ring.util.response :only(file-response)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:gen-class))

(defroutes app-routes
  (GET "/" request (file-response "index.html" {:root "resources"}))
  (route/resources "/")
  (route/files "/" {:root "public"})
  (route/not-found "Not Found"))

(def app (handler/site app-routes))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  "start the server from uberjar"
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))

