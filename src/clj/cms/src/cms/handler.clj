(ns cms.handler
  (:use [compojure.core :only [routes]]
        [compojure.route :as route :only [resources files not-found]]
        [liberator.core :refer [resource defresource]]
        [liberator.dev :refer (wrap-trace)]        
        [ring.adapter.jetty :as ring :only [run-jetty]]        
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]]        
        [ring.middleware.logger :only [wrap-with-logger]]
        [cms.controllers.document-manager :only [document-routes]]
        [cms.controllers.site-builder  :only [editor-routes]]
        [cms.api.collections :only [collection-routes]]
        [cms.api.items :only [item-routes]])
    (:gen-class))

(def env 
  "Global Environment: ENV=debug/dev/prod (dev is default)"
  (or (System/getenv "ENV") "dev"))

(def cms-routes (routes
                 editor-routes
;                        document-routes                 
;                        collection-routes
;                        item-routes
                        (route/resources "/")
                        (route/files "/" {:root "public"})
                        (route/not-found "Not Found")))

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

;;(def app (-> cms-routes
;;             wrap-params
;;             wrap-keyword-params
;;             wrap-with-logger
;;             wrap-spy))

;;(def app  (wrap-params (wrap-keyword-params  (wrap-spy (wrap-with-logger (wrap-trace cms-routes :header :ui))))))
;;(def app  (wrap-params (wrap-keyword-params  (wrap-spy (wrap-with-logger cms-routes)))))
(def app  (wrap-params (wrap-keyword-params  (wrap-with-logger cms-routes))))

(defn start-cms-mgmt [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8088"))]
    (start-cms-mgmt port)))




