(ns management.views.tools
  "Create a unordered list that will form the dom host for a bootstrap tabbed interface"
  (:require [net.cgrand.enlive-html :refer [deftemplate content clone-for do-> add-class set-attr html-content nth-of-type first-child html-snippet
                                            sniptest at wrap unwrap]]
            [management.views.snippets :refer [nav-bar]]
            ;;            [net.cgrand.reload :refer [auto-reload]]
            [flourish-common.web-page-utils :refer [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found render-snippet]]))


(deftemplate user-dashboard "templates/user_dashboard.html"
  [{:keys [display-map] :as settings}]
  [:nav.navbar] (content (nav-bar {:title "Market Manager" :menu-data (:menu-data display-map)}))
  [:ul#nav-controls-destination :li] (clone-for [display-object (:display-objects display-map)]
                                                [:a] (do->
                                                      (add-class "btn")
                                                      (add-class "btn-success")
                                                      (set-attr :href (str "#tab" (:order display-object)))
                                                      (set-attr :data-toggle "tab")
                                                      (html-content (:display-name display-object))))
  [:section.tab-content :div.tab-pane](clone-for [display-object (:display-objects display-map)]
                                                 (do->
                                                  (set-attr :id (str "tab" (:order display-object)))
                                                  (html-content (render-snippet ((eval (:view display-object)) (:model display-object))))))
  [[:ul.pages (nth-of-type 1)] :> first-child] (add-class "active")
  [[:.tab-pane first-child]] (add-class "active"))
