(ns cms.views.builder
  (:use [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]
        [net.cgrand.enlive-html]))

(defsnippet top-nav "templates/builder/navigation_bar.html" [:div.navbar-fixed-top]
  [])

(deftemplate site-builder-editor "templates/builder.html"
  [model])
;;  [:div.navbar-fixed-top] (top-nav (:nav :top model)))

(defn render [req]
  "Serve up the hosting dom for the site builder editor"
  (render-to-response (site-builder-editor req)))

