(ns landing-site.controllers.ad-network-traffic
  (:use [landing-site.cms :as cms]
        [robert.hooke :as hooke]
        [compojure.core :only (defroutes GET)]        
        [clojure.tools.logging :only (info error)]))
;;(set-session("ad-network" (params response  :ad-network)))

(defn assemble-header []
  "build a header")

(defn set-status [])

(defn view [{:keys[adnetwork campaign adgroup listing market-vector] :as settings}]
  "based on the coordinates encoded uri create the correct view response"
  (let [
        cms (cms/new-cms-filesystem {:cfg cfg})        
        site-id (get-landing-site-id cms market-vector)
        files (assemble-site-files cms site-id)
        site-content (populate-contents cms files)]
    {:headers {}
     :body site-content
     :status (set-status)}))

(defroutes landing-site-routes
  (GET "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view"
   [adnetwork campaign ad_group listing market_vector & request]
   (view {:adnetwork adnetwork  :campaign campaign :adgroup ad_group :listing listing :market-vector market_vector :request request})))


