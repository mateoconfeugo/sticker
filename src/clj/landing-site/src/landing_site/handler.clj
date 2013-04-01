(ns landing-site.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:use [ring.adapter.jetty :as ring]
        [compojure.core]
        [landing-site.controllers.user :only(user-routes) :as gui-user]
        [landing-site.views.layout :as layout]
        [landing-site.controllers.ad-network-traffic :only (landing-site-routes)]))

(defn run-of-network [] (str "run of network yall"))

(defroutes app-routes
  (context "/" [] landing-site-routes)
  (route/resources "/")
  (route/files "/" {:root "public"})
  (route/not-found [request] (run-of-network request)))

(def app (handler/site  app-routes))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))
