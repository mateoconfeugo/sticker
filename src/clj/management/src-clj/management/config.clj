(ns management.config
  ^{:author "Matthew Burns"
    :doc "Configuration settings for the managment aspects of the application"}
  (:require [ clojure.edn]
            [site-builder-udc.config :refer [apply-site-builder-configurations]]))

(defn apply-mgmt-configurations [cfg]
  (let [{:keys [mgmt-webapp-port mgmt-db-address mgmt-db-port mgmt-db-name mgmt-db-user
                mgmt-db-password mgmt-home-dir mgmt-cloud-provider mgmt-app-root-dir root-dir]} cfg]
    (-> cfg
        (assoc :mgmt-webapp-port  (or (System/getenv "MGMT_PORT") (:mgmt-webapp-port cfg)))
        (assoc :mgmt-db-address (or (System/getenv "MGMT_DATABASE_HOST") (:mgmt-db-host cfg)))
        (assoc :mgmt-db-port (or (System/getenv "MGMT_DATABASE_PORT") (:mgmt-db-port cfg)))
        (assoc :mgmt-db-name (or (System/getenv "MGMT_DATABASE_NAME") (:mgmt-db-name cfg)))
        (assoc :mgmt-db-user (or (System/getenv "MGMT_DATABASE_USERNAME") (:mgmt-db-user cfg)))
        (assoc :mgmt-db-password (or (System/getenv "MGMT_DATABASE_PASSWORD") (:mgmt-db-password cfg)))
        (assoc :mgmt-home-dir (or (System/getenv "MGMT_HOME") (System/getProperty "user.home")))
        (assoc :mgmt-cloud-provider (or (System/getenv "LSBS_PALLET_PROVIDER") :qa-cloud))
        (assoc :mgmt-app-root-dir (or (System/getenv "MGMT_HOME") (System/getProperty "user.home")))
        (assoc :root-dir (or (System/getenv "MGMT_HOME") (System/getProperty "user.dir"))))))

(defn configure-mgmt-application []
  (let [cfg (clojure.edn/read-string (slurp  "resources/config.edn")) ]
    (-> cfg
        apply-site-builder-configurations
        apply-mgmt-configurations)))
