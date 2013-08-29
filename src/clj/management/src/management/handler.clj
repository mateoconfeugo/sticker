(ns management.handler
  (:require [cemerick.friend :refer[authorize logout authenticate]]
            [cemerick.friend.workflows :refer [interactive-form]]
            [cms.api.collections :refer [collection-routes]]
            [cms.api.items :refer [item-routes]]
            [cms.controllers.document-manager]
            [cms.controllers.site-builder]            
            [compojure.core :refer [defroutes GET routes ANY]]
            [compojure.route]
            [compojure.handler :as handler]        
            [compojure.route :refer[files resources not-found]]
            [liberator.dev :refer [wrap-trace]]                
            [management.config :refer [users]]
            [management.controllers.public :refer [public-routes]]
            [management.controllers.tools :refer [user-mgmt-routes]]
            [management.controllers.admin :refer [admin-mgmt-routes]]
            [net.cgrand.enlive-html :refer[emit*]]
            [ring.adapter.jetty :as ring]
            [ring.util.response :refer[file-response redirect content-type]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]        
            [ring.middleware.logger :refer [wrap-with-logger]]                    )
  (:gen-class))        

;;(def app (handler/site (wrap-params (routes public-routes user-mgmt-routes authenticate admin-mgmt-routes))))
;;(def app (handler/site (wrap-params (routes  user-mgmt-routes))))
(defroutes config-route (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json")))

;;(def app (handler/site (wrap-params (routes))))
(def app (handler/site (routes
                        user-mgmt-routes
                        document-routes
                        cms.controllers.site-builder/editor-routes                        
                        editor-routes                                     
                        public-routes
                        config-route
                        collection-routes
                        item-routes
                        (resources "/")
                        (files "/" {:root "public"})
                        (not-found "Not Found"))))

(def app (wrap-params (wrap-keyword-params  (wrap-with-logger (wrap-trace app :header :ui)))))

;;(def app (handler/site  (routes public-routes user-mgmt-routes authenticate admin-mgmt-routes)))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "MGNT_PORT") "8087"))]
    (start port)))

(comment
(derive ::admin ::user)
(derive ::publisher ::user)
(derive ::advertiser ::user)
(derive ::content-provider ::user)
(derive ::feed-provider ::user)

  
(def app (handler/site (wrap-params (routes public-routes
                                            (authenticate user-mgmt-routes {:credential-fn (partial bcrypt-credential-fn users)
                                                                            :workflows [(interactive-form)]})
                                            (authenticate admin-mgmt-routes {:credential-fn (partial bcrypt-credential-fn admin)
                                                                             :workflows [(interactive-form)]})
                                            (logout (ANY "/logout" request (redirect "/")))))))
)
