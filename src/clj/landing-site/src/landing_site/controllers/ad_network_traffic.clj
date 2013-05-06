(ns landing-site.controllers.ad-network-traffic
  (:use [ring.util.response :only [content-type file-response]]        
        [compojure.core :only [defroutes GET]]
        [compojure.route :as route :only [not-found files resources]]
        [landing-site.core]        
        [landing-site.views.host-dom :as host :only [render]]
        [landing-site.views.static-page :only [render-static-page]]))

(defroutes landing-site-routes
  (GET "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view"
      [adnetwork campaign ad_group listing market_vector & request]  (host/render token cms))
  (GET "/static" [file] (render-static-page (str static-html-dir file) cms token))
  (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json")))

