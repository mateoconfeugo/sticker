(ns landing-site.views.host-dom
  (:use [cms.site :only [new-cms-site get-site-menu get-site-contents get-market-vector str->int get-css]]
        [clojurewerkz.urly.core :only[host-of]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]
        [landing-site.views.snippets :only[nav-bar offer]]
        [net.cgrand.enlive-html]))

(deftemplate index-with-webapp-pages "templates/index.html"
   [{:keys [site-name pages menu-data css] :as settings}]
   ;;   [:div#navbar] (content (nav-bar {:title site-name :menu-data menu-data}))
   [:ul#nav-controls-destination :li] (clone-for [p pages]
                              [:a] (do->
                                   (add-class "btn")
                                   (add-class "btn-success")
                                   (set-attr :href (str "#tab" (:order p)))
                                   (set-attr :data-toggle "tab")
                                   (html-content (:header p))))
   [:section.tab-content :div.tab-pane] (clone-for [p pages]
                                                   (do->
                                                    (set-attr :id (str "tab" (:order p)))
                                                    (html-content (:contents p))))
   [[:ul.pages (nth-of-type 1)] :> first-child] (add-class "active")
   [[:.tab-pane first-child]] (add-class "active")
   [[:li.leading] :> last-child] (content "")
   [:style#cms-css] (content css))
;;(offer {})

;; TODO: use a cache via memoization for the creation of the cms, pages and menu should speed things up quite a bit
(defn render
  [req & market-vector-id]
  "Take the sequence of pages in insert them into an unordered list"
  (let [dir landing-site.config/website-dir
        matrix-id 1
        domain (if-let [d (-> req :params :ls-url)] d (host-of (:server-name req)))
        mv-id (first (or market-vector-id (or (-> req :params :market-vector) (get-market-vector domain dir matrix-id))))
        cms (new-cms-site {:webdir dir :market-vector-id mv-id :domain-name domain})
        menu (:drop_down_menu (first (get-site-menu cms)))
        pages (get-site-contents cms)
        css (get-css cms)        
        num_pages (count pages)
        page_num (range 0 num_pages)
        pages (reverse (map #(assoc %1 :order %2)  pages page_num))]
    (render-to-response (index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages :menu-data menu :css css}))))



