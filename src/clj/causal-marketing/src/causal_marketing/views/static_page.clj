(ns causal-marketing.views.static-page
  (:use [net.cgrand.enlive-html :as html]
        [causal-marketing.views.snippets :only (nav-bar)]
        [cms.site :only [new-cms-site get-site-menu get-site-contents]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]))

(deftemplate static-html-page "templates/index.html"
  [{:keys [site-name html menu-data]}]
  [:div#navbar] (html/content (nav-bar {:title site-name :menu-data menu-data}))
  [:#rootwizard] (html/substitute (html/html-snippet html))
  [:footer] (html/content "")
  [:#sidebar] (html/content ""))

(defn render-static-page
  ([file cms id]
     (let [menu (:drop_down_menu (first (get-site-menu cms)))
           html (slurp file)]     
       (render-to-response (static-html-page {:site-name "Market With Gusto" :html html :menu-data menu})))))

(comment

(deftemplate index-with-webapp-pages "templates/index.html"
  [{:keys [site-name pages menu-data]}]
  [:#header] (html/content (nav-bar {:title site-name :menu-data menu-data}))
  [:ul.pages :li] (clone-for [p pages]
                             [:a] (do->
                                   (add-class "btn")
                                   (add-class "btn-success")
                                   (set-attr :href (str "#tab" (:order p)))
                                   (set-attr :data-toggle "tab")
                                   (content (:header p))))
  [:div.tab-content :div.tab-pane] (clone-for [p pages]
                                              (do->
                                               (set-attr :id (str "tab" (:order p)))
                                               (html-content (:contents p))))
  [[:ul.pages (nth-of-type 1)] :> first-child] (add-class "active")
  [[:.tab-pane first-child]] (add-class "active"))

(defn render
  [id cms]
  "Take the sequence of pages in insert them into an unordered list"
  (let [menu (:drop_down_menu (first (get-site-menu cms)))
        pages (get-site-contents cms)
        num_pages (count pages)
        page_num (range 0 num_pages)
        pages (reverse (map #(assoc %1 :order %2)  pages page_num))]
    (render-to-response (index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages :menu-data menu}))))
)
