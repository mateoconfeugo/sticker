(ns lead-generation.routes
  (:require [compojure.core :as c-core :refer [defroutes GET POST PUT DELETE
                                               HEAD OPTIONS PATCH ANY]]
            [compojure.route :as c-route]
            [net.cgrand.enlive-html :refer [deftemplate] :as html]            
            [shoreleave.middleware.rpc :refer [remote-ns]]
            [lead-generation.controllers.site :as cont-site]
            [lead-generation.controllers.api]))
;; Remote APIs exposed
(remote-ns 'lead-generation.controllers.api :as "api")
;; Move this to a view
(html/deftemplate lead-generation  "templates/lead_generation.html" [])
;; Controller routes, ROA oriented
(defroutes site
  (GET "/lead-generation" [] (lead-generation))  
  (GET "/" {session :session} (cont-site/index session))
  (GET "/test" [] (cont-site/test-shoreleave)))

;; Core system routes
(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes (c-core/routes site app-routes))

