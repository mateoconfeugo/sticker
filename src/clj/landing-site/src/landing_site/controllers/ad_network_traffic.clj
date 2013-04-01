(ns landing-site.controllers.ad-network-traffic
  (:use [landing-site.cms :as cms]
        [robert.hooke :as hooke]
        [compojure.core :only (defroutes GET)]        
        [clojure.tools.logging :only (info error)]))
(set-session("ad-network" (params response  :ad-network)))

(defn get-landing-site-id [market-vector]
  "from the market vector data determine the landing site"
  1)

(defn view [{:keys[adnetwork campaign adgroup listing market-vector] :as settings}]
  "based on the coordinates encoded uri create the correct view response"
  (let [site-id (get-landing-site-id :market-vector)
        cms (cms/new-cms-filesystem {:landing-site-id site-id})
        files (assemble-site-files cms (:landing-site market-vector))
        site-content (populate-contents cms files)]))
;;    {:header (assmble-header)
;;     :body site-content
;;     :status (set-status)}))

(defroutes landing-site-routes
  (GET "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:ad_group/listing/:listing/market_vector/:market_vector/view"
   [adnetwork campaign ad_group listing market_vector & request]
   (view {:adnetwork adnetwork  :campaign campaign :adgroup ad_group :listing listing :market-vector market_vector :request request})))


