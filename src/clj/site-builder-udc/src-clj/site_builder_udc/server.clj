(ns site-builder-udc.server
  "Server handles for repl development and `run` hooks"
  (:refer-clojure :exclude [read-string]) ; suppress the shadowing warning
  (:require [clojure.core :as core]
            [clojure.edn :as edn :refer [read-string]]
            [site-builder-udc.config :refer [apply-site-builder-configurations]]
            [site-builder-udc.handler :as handler]
            [ring.server.standalone :as ring-server]))

;; You'll want to do something like: `(defonce server (start-server))`
(declare ^:dynamic *cfg*)

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (binding [*cfg* (apply-site-builder-configurations (edn/read-string (slurp "resources/config.edn")))]
    (let [port (or (and port (Integer/parseInt port))
                   (Integer. (get (System/getenv) "PORT" (*cfg* :site-builder-port)))
                   8080)
          server (ring-server/serve (handler/get-handler #'handler/app)
                                    {:port port
                                     :init handler/init
                                     :auto-reload? true
                                     :destroy handler/destroy
                                     :join true})]
      server)))

(defn stop-server [server]
  (when server
    (.stop server)
    server))

(defn restart-server [server]
  (when server
    (doto server
      (.stop)
      (.start))))

(def server-starters {:dev start-server})

(defn -main [& m]
  (let [mode-kw (keyword (or (first m) :dev))
        server-fn (server-starters mode-kw)
        server (server-fn)]
    server))
