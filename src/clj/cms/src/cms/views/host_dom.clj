(ns cms.views.host-dom
  (:use [flourish-common.web-page-utils :only [render-to-response] :as fc]
        [net.cgrand.enlive-html :only [deftemplate] :as html]))

(html/deftemplate site-builder-editor "templates/builder.html"
  [{:keys [] :as settings}])

(defn render [req]
  "Serve up the hosting dom for the site builder editor"
  (fc/render-to-response (site-builder-editor req)))

