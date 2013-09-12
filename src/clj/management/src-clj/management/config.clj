(ns management.config
  "Configuration component that reads a json file when created"
  (:use [cheshire.core]
        [cemerick.friend.workflows :only[interactive-form]]
        [cemerick.friend.credentials :only[hash-bcrypt]]))

(def users {"root" {:username "root"
                    :password (hash-bcrypt "sa")
                    :roles #{::admin}}
            "paul" {:username "paul"
                    :password (hash-bcrypt "sa")
                    :roles #{::publisher}}
            "ann" {:username "ann"
                    :password (hash-bcrypt "sa")
                   :roles #{::advertiser}}
            "carl" {:username "carl"
                    :password (hash-bcrypt "sa")
                    :roles #{::content-provider}}
            "freddy" {:username "freddy"
                    :password (hash-bcrypt "sa")
                    :roles #{::feed-provider}}
            "jane" {:username "jane"
                    :password (hash-bcrypt "sa")
                    :roles #{::user}}})

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

(def db-address (or (System/getenv "MGMT_DATABASE_HOST") "127.0.0.1"))
(def db-port (or (System/getenv "MGMT_DATABASE_PORT") 3306))
(def db-name (or (System/getenv "MGMT_DATABASE_NAME") "test_mgmt"))
(def db-user (or (System/getenv "MGMT_DATABASE_USERNAME") "root"))
(def db-password (or (System/getenv "MGMT_DATABASE_PASSWORD") "test123"))
