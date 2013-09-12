(ns heat-mapping.routes
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.route :as c-route]
            [cheshire.core :refer [generate-string parse-string]]            
            [net.cgrand.enlive-html :refer [deftemplate html-content]]            
            [shoreleave.middleware.rpc :refer [remote-ns]]
            [heat-mapping.controllers.site :as cont-site]
            [heat-mapping.controllers.api :refer [handle-coord]]))

(remote-ns 'heat-mapping.controllers.api :as "api/heat-mapping")

(defn get-landing-site [landing-site-id] "<html><body><div id='analysis-area'>foo</div></body><html>")

(deftemplate heat-map  "templates/heat_mapping.html"
  [landing-site-html]
  [:div#analysis-area] (html-content landing-site-html))

(defroutes landing-site-delivery-routes
  (POST "/heatmap" {body :body} (handle-coord (parse-string (slurp body) true))))

(defroutes mgmt-site-routes
  (GET "/heat-map/:ls-id" [ls-id] (heat-map (get-landing-site ls-id)))
  (GET "/" {session :session} (cont-site/index session))
  (GET "/test" [] (cont-site/test-shoreleave)))

(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes (routes mgmt-site-routes app-routes))

