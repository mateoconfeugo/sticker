(ns landing-site.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:use [ring.adapter.jetty :as ring]
        [compojure.core :only (defroutes routes GET POST)]        
        [ring.util.response :as resp]
        [net.cgrand.enlive-html :as html]
        [flourish-common.config]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(def app (handler/site  (routes
                         lead-gen-routes                 
                         landing-site-routes
                         (route/resources "/")
                         (route/files "/" {:root "public"})
                         (route/not-found "Not Found"))))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))
