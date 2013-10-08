(ns management.handler
  ^ {:author "Matthew Burns"
     :doc "Top level handler that brings together the routes of the various user interface toolsets
           the application configuration and the web server host"}
    (:require [management.routes :refer [webapp]]
              [management.config :refer [configure-mgmt-application]]
              [ring.adapter.jetty :as ring :refer [run-jetty]])
        (:gen-class))

(declare ^:dynamic *cfg*)

(defn start [web-application]
  (let [cfg (configure-mgmt-application)]
    (binding [*cfg* cfg]
      (ring/run-jetty web-application {:port (:mgmt-webapp-port cfg) :join? false}))))

(defn -main [] (start webapp))

(def app webapp)
