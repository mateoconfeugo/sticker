(ns site-builder-udc.editor
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]])
  (:require [shoreleave.remotes.http-rpc :as rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]            
            [shoreleave.remote]))

;;(defn ^:export save-landing-site [{:keys [layout xpath dom] :as args}]
(defn ^:export save-landing-site [layout xpath dom]
  (srm/rpc
   (api/save {:layout layout :xpath xpath :dom dom}) [resp]
   (js/alert (str "XPath: "  (:xpath resp)  " Layout: " (:layout resp) " Dom: " (:dom resp)))))
