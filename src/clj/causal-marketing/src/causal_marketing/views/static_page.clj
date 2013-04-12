(ns causal-marketing.views.static-page
  (:use [net.cgrand.enlive-html :as html]
        [causal-marketing.views.snippets :only (nav-bar)]
        [cms.site :only [new-cms-site get-site-menu get-site-contents]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]))

(deftemplate static-html-page "templates/index.html"
  [{:keys [site-name html menu-data]}]
  [:#header] (html/content (nav-bar {:title site-name :menu-data menu-data}))
  [:#rootwizard] (str html))

(defn render
  ([file cms id]
     (let [menu (:drop_down_menu (first (get-site-menu cms id)))
           html (slurp file)]     
       (render-to-response (static-html-page {:site-name "Causal Marketing" :html html :menu-data menu})))))

