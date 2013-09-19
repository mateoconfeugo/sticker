(ns site-builder-udc.main-upload
;;  (:require-macros [enfocus.macros :as em])
  (:require
   ;;[enfocus.core :as ef :refer [at content get-text from]]
;;            [enfocus.events :as ev :refer [listen]]
            [site-builder-udc.editor :refer [save-landing-site]]
            [site-builder-udc.repl :as repl :refer [connect-app]]))
(comment
(defn gather-tuple [node]
    {:xpath (js/getXPath node) :dom node :layout (ef/from node (ef/get-text))})

(defn wire-up-events []
  "Hook up the handler functions for those selected element events"
  (ef/at "#shareModal" (ev/listen :click #(ef/at ".demo" (ef/content (save-landing-site (gather-tuple %)))))))
)

(defn start []
  "Application driver that initializes, hooks up event handlers to elements  and starts the application going"           
  (do
;;    (repl/connect-app )
;;    (wire-up-events)
    (js/initialize_legacy_editor)))

(set! (.-onload js/window) start)