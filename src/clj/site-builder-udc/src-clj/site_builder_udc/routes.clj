(ns site-builder-udc.routes
  (:require [compojure.core :as c-core :refer [defroutes GET POST PUT DELETE
                                               HEAD OPTIONS PATCH ANY]]
            [compojure.route :as c-route]
            [net.cgrand.enlive-html :refer [deftemplate] :as html]            
            [shoreleave.middleware.rpc :refer [remote-ns]]
            [site-builder-udc.controllers.site :as cont-site :refer [test-shoreleave]]
            [site-builder-udc.controllers.api]
            [site-builder-udc.views.editor :refer [editor site-builder]]))

;; Remote APIs exposed
(remote-ns 'site-builder-udc.controllers.api :as "api")

;; Move this to a view
;;(html/deftemplate site-builder  "templates/builder.html" [])

;; Controller routes, ROA oriented
(defroutes site
  (GET "/site-builder" [] (site-builder))
  (GET "/editor" [] (editor {}))    
;;  (GET "/" {session :session} (cont-site/index session))
  (GET "/test" [] (cont-site/test-shoreleave)))

;; Core system routes
(defroutes app-routes
  (c-route/resources "/")
  (c-route/resources "/design/" {:root "templates"})
  (c-route/not-found "404 Page not found."))

(def all-routes (c-core/routes site app-routes))

