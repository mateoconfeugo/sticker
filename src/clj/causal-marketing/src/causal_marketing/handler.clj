(ns causal-marketing.handler
  (:use [compojure.core]
        [ring.adapter.jetty :as ring]
        [ring.util.response :as resp]
        [net.cgrand.enlive-html :as html]
        [flourish-common.config]
        [causal-marketing.controllers.site  :only [site-routes]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:gen-class))

(def app (handler/site site-routes))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  "start the server from uberjar"
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))

(defonce server (run-jetty #'app {:port 8087 :join? false}))