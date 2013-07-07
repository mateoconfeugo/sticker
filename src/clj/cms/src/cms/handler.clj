(ns cms.handler
  (:use [cms.controllers.document-manager :as dm]
        [cms.controllers.site-builder :as sb]
        [compojure.core :only [routes]]
        [compojure.route :as route]
        [ring.adapter.jetty :as ring]        
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]]        
        [ring.middleware.logger :only [wrap-with-logger]]))


(def app-routes (routes dm/document-routes
                        sb/editor-routes
                        (route/resources "/")
                        (route/files "/" {:root "public"})
                        (route/not-found "Not Found")))

(def app (-> app-routes
             wrap-params
             wrap-keyword-params
             wrap-with-logger))

(defn start-lsbs [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8088"))]
    (start-lsbs port)))