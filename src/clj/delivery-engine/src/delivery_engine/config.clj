(ns delivery-engine.config
  (:use [cheshire.core]))
                                        ; ENVIRONMENT VARIABLES
;;(def ^{:dynamic true} cfg-file-path (System/getenv "DELIVERY_ENGINE_CFG_FILE"))
(def ^{:dynamic true} cfg-file-path "/Users/matthewburns/Sandbox/clojure/delivery-engine/delivery_config.json")

                                        ; CONFIGURATION
(defn read-custom-config
  "Read and transform  json data into a nested map"
  [file]
  (parse-string (slurp file) true))

(def config (partial read-custom-config cfg-file-path))

(defn gen-cache-key
  "create a cache key"
  [entity]
  )