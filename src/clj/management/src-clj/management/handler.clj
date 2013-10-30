(ns management.handler
  ^ {:author "Matthew Burns"
     :doc "Top level handler that brings together the routes of the various user interface toolsets
           the application configuration and the web server host"}
    (:require [management.routes :refer [webapp]]
              [management.signup-and-redirect :refer [page]]
              [management.config :refer [configure-mgmt-application]]
              [ring.adapter.jetty :as ring :refer [run-jetty]]
              [ring.middleware.reload :refer [wrap-reload]])
        (:gen-class))

(declare ^:dynamic *cfg*)


;;(stencil.loader/set-cache (clojure.core.cache/ttl-cache-factory {} :ttl 0))

(defn start [web-application]
  (let [cfg (configure-mgmt-application)]
    (binding [*cfg* cfg]
      (ring/run-jetty (wrap-reload  web-application) {:port (:mgmt-webapp-port cfg) :join? false}))))

;;(defn -main [] (start webapp))
(defn -main [] (start page))

;;(def app webapp)
(def app page)
