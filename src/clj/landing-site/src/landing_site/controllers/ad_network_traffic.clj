(ns landing-site.controllers.ad-network-traffic
  (:import [java.io IOException])
  (:use [compojure.core :only [defroutes GET ANY]]
        [compojure.route :as route :only [not-found files resources]]
        [landing-site.config]
        [landing-site.views.host-dom :as host-dom :only [render]]
        [landing-site.views.static-page :only [render-static-page]]
        [riemann.client :only [send-event tcp-client]]
        [ring.util.response :only [content-type file-response response]]))

(defn handle-adnetwork-traffic
  [{:keys [adnetwork campaign ad-group listing market-vector session] :as settings}]
  "Wrap the biz logic with the session setup"
  (let [new-session (assoc session :adnetwork adnetwork :campaign campaign :ad-group ad-group
                           :listing listing :market-vector market-vector :session session)]
    (if-let [monitoring-bus (try (tcp-client) (catch IOException e))]
      (riemann.client/send-event monitoring-bus {:service "clicks" :state "ok" :tags["landing-site"]
                                                 :metric 1 :description "traffic generated from adnetwork"}))
    (->  (host-dom/render token cms) (assoc :session new-session))))

(defroutes landing-site-routes
  (GET "/" [] (host-dom/render token cms))
  (GET "/static" [file] (render-static-page (str static-html-dir file) cms token))
  (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json"))
  (GET "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view"
      [adnetwork campaign ad_group listing market_vector :as {session :session}]
    (handle-adnetwork-traffic {:adnetwork adnetwork :campaign campaign :ad-group ad_group
                               :market-vector market_vector :session session})))

(comment  
(defn get-customized-site-pages
  [{:keys [landing-site-id page-number token market-id adgroup cms] :as settings}]
  (let [pages (get-site-contents cms token)
        path (if (and (exists? page-number) (exists? landing-site-id))
               (str root-dir "/landing_site/" landing-site-id "/" landing-site-id "/" page-number ".html")
               (str root-dir "/landing_site/" landing-site-id "/" landing-site-id "/" landing-site-id ".html"))
        selected-page (slurp path)]
    {:selected-page selected-page :pages pages})))
           
