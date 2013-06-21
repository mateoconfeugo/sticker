(ns landing-site.views.host-dom
  (:use [net.cgrand.enlive-html]
        [landing-site.views.snippets :only[nav-bar offer]]        
        [cms.site :only [new-cms-site get-site-menu get-site-contents get-market-vector str->int]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]))

(deftemplate index-with-webapp-pages "templates/index.html"
   [{:keys [site-name pages menu-data] :as settings}]
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
   [[:li.leading] :> last-child] (content ""))
;;(offer {})

(defn render
  [req & market-vector-id]
  "Take the sequence of pages in insert them into an unordered list"
  (let [dir landing-site.config/website-dir
        matrix-id 1
        domain (if-let [d (-> req :params :ls-url)] d (str (:server-name req) "/"))
        mv-id (or market-vector-id (or (str->int (-> req :params :market-vector)) (get-market-vector domain dir matrix-id)))
        cms (new-cms-site {:webdir dir :market-vector-id mv-id :domain-name domain})
        menu (:drop_down_menu (first (get-site-menu cms)))
        pages (get-site-contents cms)
        num_pages (count pages)
        page_num (range 0 num_pages)
        pages (reverse (map #(assoc %1 :order %2)  pages page_num))]
    (render-to-response (index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages :menu-data menu}))))


