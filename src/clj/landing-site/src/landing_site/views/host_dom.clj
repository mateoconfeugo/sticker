(ns landing-site.views.host-dom
  (:use [cms.site]
;;        [clojurewerkz.urly.core :only[host-of]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]
        [landing-site.views.snippets :only[nav-bar offer]]
        [net.cgrand.enlive-html]))

(deftemplate index-with-webapp-pages "templates/index.html"
  [{:keys [site-name pages menu-data css fonts header-image-path
           modal-form side-form conversion-scripts site-title site-banner] :as settings}]
  ;;   [:div#navbar] (content (nav-bar {:title site-name :menu-data menu-data}))
  [:title#site-title] (content site-title)
  [:h3.site-banner] (content site-banner)
  [:script.conversion-script] (content conversion-scripts)
  [:img#header-image] (set-attr :src header-image-path)
  [:div#myModal](substitute (html-snippet (:form_builder_html modal-form)))
  [:aside#side-lead-form] (substitute (html-snippet (:form_builder_html side-form)))
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
;;   [:.tab-pane :li.leading] (append "yummy")
   [[:li.leading] :> last-child] (content "")
   [:style#cms-css] (content css)
   [:head :> (attr-has :src "replace-me")] (clone-for [f fonts]
                                                     (do->                                                     
                                                     (set-attr :href f)
                                                     (set-attr :rel "stylesheet")
                                                     (set-attr :type "text/css"))))
;;(offer {})

;; TODO: use a cache via memoization for the creation of the cms, pages and menu should speed things up quite a bit
(defn render
  [req & market-vector-id]
  "Take the sequence of pages in insert them into an unordered list"
  (let [cms (-> req :params :cms)
;;        menu (:drop_down_menu (first (get-site-menu cms)))
        pages (get-site-contents cms)
        css (get-css cms)
        image-path (get-header-image cms)
        fonts (get-fonts cms)
        modal (get-modal-form cms)
        side-form (get-side-form cms)
        scripts (get-conversion-scripts cms)
        site-title (get-site-title cms)
        site-banner (get-site-banner cms)
        num_pages (count pages)
        page_num (range 0 num_pages)
        pages (reverse (map #(assoc %1 :order %2)  pages page_num))
        opts {:site-name "MarketWithGusto.com" :pages pages 
              :css css :fonts fonts :header-image-path image-path
              :modal-form modal :side-form side-form :conversion-scripts scripts
              :site-title site-title :site-banner site-banner}]
    (render-to-response (index-with-webapp-pages opts))))


;; :menu-data menu
    

