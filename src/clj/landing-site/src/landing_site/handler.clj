(ns landing-site.handler
  "This is the top level application driver it combines all the controllers
   together and those routes form the response behaviour of the web app.  Also
   the  startup and shutdown commands"
  (:require [compojure.handler :as handler]
            [compojure.response]
            [compojure.route :as route])
  (:use [compojure.core :only (routes)]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]]
        [landing-site.controllers.conversion :only[conversion-routes]]
;;        [landing-site.controllers.heatmap :only[heatmap-routes]]                
        [riemann.client :only [tcp-client send-event]]
        [ring.adapter.jetty :as ring]
        [ring.middleware.logger]
        [ring.middleware.params :only [wrap-params]]
        [ring.util.response :as resp])
  (:gen-class ))

(def app (handler/site (wrap-with-logger (wrap-params (routes  
                                                               lead-gen-routes                 
                                                               landing-site-routes
                                                               conversion-routes
                                                               ;;                                      heatmap-routes
                                                               (route/resources "/")
                                                               (route/files "/" {:root "public"})
                                                               (route/not-found "Not Found"))))))

(defn start-lsbs [port]
  (run-jetty app {:port port :join? false}))

;; TODO:  put a monitoring event here and one in corresponding shutdown
;; method that is also needed.
(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8087"))]
    (start-lsbs port)))
