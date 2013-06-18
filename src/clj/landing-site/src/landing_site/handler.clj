(ns landing-site.handler
  "This is the top level application driver it combines all the controllers
   together and those routes form the response behaviour of the web app.  Also
   the  startup and shutdown commands"
  (:require [compojure.handler :as handler]
            [compojure.response]
            [compojure.route :as route])
  (:use [compojure.core :only (routes)]
        [cms.site :only [new-cms-site]] 
	[landing-site.config :only [website-dir token-type site-tag site-name]]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]]
        [landing-site.controllers.conversion :only[conversion-routes]]
;;        [landing-site.controllers.heatmap :only[heatmap-routes]]                
        [riemann.client :only [tcp-client send-event]]
        [ring.adapter.jetty :as ring]
        [ring.middleware.logger]
        [ring.middleware.params :only [wrap-params]]
        [ring.util.response :as resp]
        [clojurewerkz.urly.core :only [host-of]])
  (:gen-class ))

(def web-app (handler/site (wrap-with-logger (wrap-params (routes  
						       lead-gen-routes                 
						       landing-site-routes
						       conversion-routes
						       ;;                                      heatmap-routes
						       (route/resources "/")
						       (route/files "/" {:root "public"})
						       (route/not-found "Not Found"))))))

(defn determine-site
  [req]
  "figure out which landing site to use"
   (let [query-str-url (-> req :params :ls-url)
         domain-name (str (host-of (:uri req)) "/")]
  (if (or query-str-url domain-name)
       (or query-str-url domain-name))))


(defn cms-wrapper 
  [app]
  "Adds the cms client to the request so that a user can
   have many sites"
  (fn [req]
    (let [domain-name  (or (determine-site req) "patientcomfortway.com")
          base-path-dir (str website-dir "/" domain-name "site")
          cms (new-cms-site {:base-path base-path-dir  :site-cfg-path token-type :site-tag site-tag :site-name site-name})]
    (app (assoc req :cms cms)))))

(def app (-> web-app cms-wrapper))

(defn start-lsbs [port]
  (run-jetty app {:port port :join? false}))

;; TODO:  put a monitoring event here and one in corresponding shutdown
;; method that is also needed.
(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8087"))]
    (start-lsbs port)))
