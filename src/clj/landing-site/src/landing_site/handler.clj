(ns landing-site.handler
  "This is the top level application driver it combines all the controllers
   together and those routes form the response behaviour of the web app.  Also
   the  startup and shutdown commands"
  (:require [compojure.handler :only[site] :as handler]
            [ringmon.monitor :as monitor]
            [compojure.route :as route])
  (:use [compojure.core :only (routes routing defroutes GET)]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]]
        [landing-site.controllers.conversion :only[conversion-routes]]
;        [landing-site.controllers.heatmap :only[heatmap-routes]]                
        [ring.adapter.jetty :as ring]
        [ring.middleware.cookies        :only [wrap-cookies]]
	[ring.middleware.content-type]
        [ring.middleware.logger :only [wrap-with-logger]]
        [ring.middleware.session :only [wrap-session]]
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]])
  (:gen-class ))

(def web-app (handler/site (routes lead-gen-routes                 
                                   landing-site-routes
                                   conversion-routes
;                                   heatmap-routes                               
                                   (route/resources "/")
                                   (route/files "/" {:root "public"})
                                   (route/not-found "Not Found"))))

(def app (-> web-app
             wrap-cookies
             wrap-params             
             wrap-keyword-params
             wrap-session
             wrap-with-logger
             monitor/wrap-ring-monitor
	     ))

(defn start-lsbs [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8087"))]
    (start-lsbs port)))

