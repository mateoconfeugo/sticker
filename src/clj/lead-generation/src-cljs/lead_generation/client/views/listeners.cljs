(ns lead-generation.client.views.listeners
  "Attach listeners to items of interest in the DOM"
  (:require [clojure.browser.dom :as c-dom :refer [get-element]]
            [clojure.browser.event :as c-event :refer [listen]]
            [shoreleave.remotes.http-rpc :as rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]            
            [shoreleave.remote])
  (:require-macros [shoreleave.remotes.macros :as srm]))

(defn attach-view-detail [f]
  (c-event/listen (c-dom/get-element "mainbutton") 
                  "click" #(f %))) ;; this turns our "f" into a callback

(def attach-view-summary (c-event/listen (c-dom/get-element "shareModal") 
                               "click" #(srm/rpc
                                         (api/save "layout" "xpath" "dom") [updated-landing-site]
                                         (js/alert (str "Results: "  (:two updated-landing-site))))))
