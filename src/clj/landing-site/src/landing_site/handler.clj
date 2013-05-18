(ns landing-site.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.route :as route])
  (:use [ring.adapter.jetty :as ring]
        [compojure.core :only (defroutes routes GET POST)]        
        [ring.util.response :as resp]
        [net.cgrand.enlive-html :as html]
        [riemann.client :only [tcp-client send-event]]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]]
        [landing-site.views.thank-you :only[render] :as thank-you]
        [ring.middleware.params         :only [wrap-params]] )        
  (:gen-class ))

(defroutes conversion-routes
  (GET "/thank-you/:name" [name] (thank-you/render name)))


(def app (handler/site (wrap-params (routes
                         lead-gen-routes                 
                         landing-site-routes
                         conversion-routes
                         (route/resources "/")
                         (route/files "/" {:root "public"})
                         (route/not-found "Not Found")))))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))
