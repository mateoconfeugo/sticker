(ns landing-site.core
  "Application level objects and settings"
  (:use [cms.site :only (new-cms-site)]
        [flourish-common.config]))

;; resource locations
(def home-dir (str (System/getProperty "user.home") "/website"))
(def root-dir (str (System/getProperty "user.dir") "/website"))
(def cfg-dir (str (System/getProperty "user.dir") "/website/config"))
(def static-html-dir (str (System/getProperty "user.dir") "/website/site/articles/"))

;; settings
(def cfg (read-config (new-config {:cfg-file-path (str cfg-dir "/site-config.json")})))
(def token-type (-> cfg :website :site-cfg-dir-path))
(def token (-> cfg :website :market_vector))
(def site-tag (-> cfg :website :site-tag))
(def site-name (-> cfg :website :site-name))

;; application objects
(def cms (new-cms-site {:base-path root-dir :site-cfg-path token-type :site-tag site-tag :site-name site-name}))