(ns landing-site.controllers.ad-network-traffic
  (:use [landing-site.core]
        [robert.hooke :as hooke]
        [compojure.core :only (defroutes GET)]        
        [clojure.tools.logging :only (info error)]
;;(set-session("ad-network" (params response  :ad-network)))
        [landing-site.views.host-dom :as host :only (render)]
        [landing-site.views.static-page :only(render-static-page)]))


(def uri-path-schema "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view")
(def ad-network-params )

(defroutes landing-site-routes
  (GET uri-path-schema
      [adnetwork campaign ad_group listing market_vector & request]
    (host-dom/render token cms))
  (GET "/static" [file] (render-static-page (str static-html-dir file) cms token))
  (route/resources "/")
  (route/not-found "Not Found"))

    
;;       (view {:adnetwork adnetwork  :campaign campaign :adgroup ad_group :listing listing :market-vector market_vector :request request})))


(defn view [{:keys[adnetwork campaign adgroup listing market-vector] :as settings}]
  "based on the coordinates encoded uri create the correct view response"
(comment  
  (let [
        site-id (get-landing-site-id cms market-vector)
        files (assemble-site-files cms site-id)
        site-content (populate-contents cms files)]
    {:headers {}
     :body site-content
     :status (set-status)})
)
  )



