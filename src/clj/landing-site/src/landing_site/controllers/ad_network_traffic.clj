(ns landing-site.controllers.ad-network-traffic
  (:import [javax.imageioImageIO]
           [java.io File ByteArrayOutputStream IOException ByteArrayInputStream FileInputStream]
           [org.apache.commons.codec.binary.Base64])
  (:use [cms.site :only[str->int]]
        [compojure.core :only [defroutes GET ANY]]
        [compojure.route :as route :only [not-found files resources]]
        [landing-site.config]
        [landing-site.views.host-dom :as host-dom :only [render]]
        [landing-site.views.static-page :only [render-static-page]]
        [riemann.client :only [send-event tcp-client]]
        [ring.util.response :only [content-type file-response response header]]))

(comment
 (if-let [monitoring-bus (try (tcp-client) (catch IOException e))]
            (riemann.client/send-event monitoring-bus {:service "clicks" :state "ok" :tags["landing-site"]
                                                 :metric 1 :description "traffic generated from adnetwork"})))


(defn handle-adnetwork-traffic
  [{:keys [request adnetwork campaign ad-group listing market-vector] :as settings}]
  "Wrap the biz logic with the session setup"
  (let [new-session (assoc (:session request) :adnetwork adnetwork :campaign campaign :ad-group ad-group
                           :listing listing :market-vector market-vector)
        req (assoc request :session new-session)]
    (->  (host-dom/render req market-vector))))

(defn serve-image
  [request file ext]
  "Gets the image for the domain landing site"
  {:body (FileInputStream. (str (-> request :params :img-dir) "/" file "." ext))
   :status 200
   :headers {"Content-Type" (str "image/" ext)}})

(defroutes landing-site-routes
  (GET "/img/:file.:ext" [file ext :as req] (serve-image req file ext))
  (GET "/static" [file :as req] (render-static-page (str static-html-dir file) (-> req :params :cms)))
  (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json"))
  (GET "/" req (host-dom/render req))
  (GET "/adnetwork/:adnetwork/campaign/:campaign/adgroup/:adgroup/listing/:listing/market_vector/:market_vector/view"
      [adnetwork campaign ad_group listing market_vector :as req]
    (handle-adnetwork-traffic {:request req :adnetwork adnetwork :campaign campaign :ad-group ad_group :market-vector market_vector})))

(comment  
(defn get-customized-site-pages
  [{:keys [landing-site-id page-number token market-id adgroup cms] :as settings}]
  (let [pages (get-site-contents cms token)
        path (if (and (exists? page-number) (exists? landing-site-id))
               (str root-dir "/landing_site/" landing-site-id "/" landing-site-id "/" page-number ".html")
               (str root-dir "/landing_site/" landing-site-id "/" landing-site-id "/" landing-site-id ".html"))
        selected-page (slurp path)]
    {:selected-page selected-page :pages pages})))
           
