(ns landing-site.handler
  "This is the top level application driver it combines all the controllers
   together and those routes form the response behaviour of the web app.  Also
   the  startup and shutdown commands"
  (:refer-clojure :exclude [resolve])  
  (:require [compojure.handler :only[site] :as handler]
            [ringmon.monitor :as monitor]
            [clojure.pprint]
            [compojure.route :as route])
  (:use [compojure.core :only (routes routing defroutes GET)]
        [clojurewerkz.urly.core]
        [cms.site :only[str->int get-market-vector new-cms-site]]
        [landing-site.config]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]
        [landing-site.controllers.lead :only[lead-gen-routes]]
        [landing-site.controllers.conversion :only[conversion-routes]]
;        [landing-site.controllers.heatmap :only[heatmap-routes]]                
        [ring.adapter.jetty :as ring]
        [ring.middleware.cookies        :only [wrap-cookies]]
	[ring.middleware.content-type]
        [ring.middleware.file]
        [ring.middleware.logger :only [wrap-with-logger]]
        [ring.middleware.session :only [wrap-session]]
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]])
  (:gen-class ))

(def app-routes (routes lead-gen-routes                 
                                   landing-site-routes
                                   conversion-routes
;                                   heatmap-routes                               
                                   (route/resources "/")
                                   (route/files "/" {:root "public"})
                                   (route/not-found "Not Found")))

(defn wrap-cms [app]
  (fn [req]
    (let [dir landing-site.config/website-dir
          ;;          domain (or (-> req :params :ls-url) (host-of (:server-name req)))
          domain "patientcomfortreferral.com"
          img-dir (str dir "/" domain "/img")]
          (if (or (-> req :params :ls-url)(-> req :params :market_vector))
            (let [matrix-id 1
                  ;;                  mv-id  (or (-> req :params :market_vector) (get-market-vector domain dir matrix-id))
                  mv-id  1
                  ;;                  cms (new-cms-site {:webdir dir :market-vector-id mv-id :domain-name domain})
                  cms (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 2})
;;                  cms (new-cms-site {:webdir dir :market-vector-id 2 :domain-name "patientcomfortreferral.com"})                  
                  new-params (assoc (:params req) :cms cms :img-dir img-dir)]
              (app (assoc req :params new-params)))
            (let [new-params (assoc (:params req) :img-dir img-dir)]
              (app (assoc req :params new-params)))))))

(defn wrap-spy [handler]
  (fn [request]
    (println "-------------------------------")
    (println "Incoming Request:")
    (clojure.pprint/pprint request)
    (let [response (handler request)]
      (println "Outgoing Response Map:")
      (clojure.pprint/pprint response)
      (println "-------------------------------")
      response)))


(def app  (wrap-params (wrap-keyword-params (wrap-cms (wrap-with-logger app-routes)))))

(defn start-lsbs [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8087"))]
    (start-lsbs port)))

