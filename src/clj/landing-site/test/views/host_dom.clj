(ns views.host-dom
  (:use [landing-site.views.host-dom :as host]
        [landing-site.views.snippets]
        [expectations]
        [landing-site.config]
        [cms.site]
        [korma.core :only [defentity database insert values select where] :as kdb]
        [korma.db :only [defdb mysql]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request render-snippet]]))
    

(def html-output (host/render token cms))
(def pages (assemble-site-files root-dir site-tag site-name))
(def pages-contents (populate-contents root-dir token-type site-tag pages token ))
(def menu (:drop_down_menu (first (get-site-menu cms token))))
(get-site-contents cms token)

(first pages-contents)
(def output (render-to-response (host/index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages-contents :menu-data menu})))
(render-snippet offer "blah")
(offer {})