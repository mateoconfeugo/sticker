(ns causal-marketing.controllers.site
  (:require         [causal-marketing.views.host-dom :only (render)]
                    [causal-marketing.views.static-page :only (render)])
  (:use [cms.site :only (new-cms-site)]
        [compojure.core]
        [ring.util.response :only(file-response)]
        [compojure.route :as route :only [not-found files resources]]
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

(defroutes site-routes
  (GET "/home" [] (causal-marketing.views.host-dom/render token cms))
  (GET "site/articles/:file" [file] (causal-marketing.views.static-page/render (str static-html-dir file) cms token))
  ;;  (route/files "/" {:root (str root-dir "/site/articles")})
    (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

