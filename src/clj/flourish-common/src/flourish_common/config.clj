(ns flourish-common.config
  "Configuration component that reads a json file when created"
  (:use [cheshire.core]))

;; INTERFACE SPECIFICATION
(defprotocol Config
  "Configuration interface"
  (read-config [this]
      "Read and transform  json data into a nested map"))

;; BACK-END IMPLEMENTATION 
(defn read-custom-config
  "Read and transform  json data into a nested map"
  [file]
  (parse-string (slurp file) true))

;; IMPLEMENTATION CONSTRUCTOR
(defn new-config
  [{:keys[cfg-env-variable cfg-file-path] :as settings}]
(let [path (if cfg-file-path  cfg-file-path (System/getenv cfg-env-variable))
      cfg (read-custom-config path)]
  (reify Config
    (read-config [this ]
      cfg))))

