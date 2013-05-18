(ns dev-ops.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.adapter.jetty :as ring])
  (:gen-class))

(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>"))

(defn -main []
    (run-jetty routes {:port 3000 :join? false}))