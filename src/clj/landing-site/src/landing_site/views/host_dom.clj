(ns landing-site.views.host-dom
  (:use [net.cgrand.enlive-html]
        [landing-site.views.snippets :only[nav-bar]]        
        [cms.site :only [new-cms-site get-site-menu get-site-contents]]
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
   [[:.tab-pane first-child]] (add-class "active"))

(defn render
  [id cms]
  "Take the sequence of pages in insert them into an unordered list"
  (let [menu (:drop_down_menu (first (get-site-menu cms id)))
        pages (get-site-contents cms id)
        num_pages (count pages)
        page_num (range 0 num_pages)
        pages (reverse (map #(assoc %1 :order %2)  pages page_num))]
    (render-to-response (index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages :menu-data menu}))))


;;(def-landing-site "templates/index.html")
;; TODO: Make this generic so we can have all sorts of customizable landing pages
(comment
(defn map-elements-into-dom
  [{:keys [:template :settings :mappings]}]
  "maps the user configured landing site components into the dom declared by the template"))

  ;;  (map-elements-into-dom {:template "templates/index.html" :settings settings}
  ;;  [: (nth-of-type 1)]  (html/content (nav-bar {:title site-name :menu-data menu-data}))

