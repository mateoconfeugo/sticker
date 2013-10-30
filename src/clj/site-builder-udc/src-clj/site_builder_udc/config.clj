(ns site-builder-udc.config)

(defn apply-site-builder-configurations [cfg]
  (let [{:keys [site-builder-db-host site-builder-db-port site-builder-db-username-prefix
                site-builder-api-key site-builder-db-api-password]} cfg]
    (-> cfg
        (assoc :site-builder-db-host (or (System/getenv "SITE_BUILDER_DATABASE_HOST") (:db-host cfg)))
        (assoc :site-builder-db-port (or (System/getenv "SITE_BUILDER_DATABASE_PORT") (:db-port cfg)))
        (assoc :site-builder-db-username-prefix (or (System/getenv "SITE_BUILDER_DATABASE_NAME_PREFIX") (:db-name-prefix cfg)))
        (assoc :site-builder-db-api-key (or (System/getenv "SITE_BUILDER_DATABASE_API_KEY") (:api-key cfg)))
        (assoc :site-builder-db-api-password (or (System/getenv "SITE_BUILDER_DATABASE_API_PASSWORD") (:api-password cfg))))))
