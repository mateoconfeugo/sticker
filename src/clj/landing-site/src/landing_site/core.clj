(ns landing-site.core
  (:use [cms.site :only (new-cms-site)]
        [flourish-common.config]))

(def root-dir (str (System/getProperty "user.dir") "/website"))
(def cfg-dir (str (System/getProperty "user.dir") "/website/config"))
(def static-html-dir (str (System/getProperty "user.dir") "/website/site/articles/"))
(def cfg (read-config (new-config {:cfg-file-path (str cfg-dir "/site-config.json")})))
(def token-type (-> cfg :website :site-cfg-dir-path))
(def token (-> cfg :website :market_vector))
(def site-tag (-> cfg :website :site-tag))
(def site-name (-> cfg :website :site-name))
(def cms (new-cms-site {:base-path root-dir :site-cfg-path token-type :site-tag site-tag :site-name site-name}))