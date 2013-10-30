(ns site-builder-udc.main-upload
  (:require [jayq.core :refer [$ text val on prevent remove-class add-class remove empty html children append]]
;;            [site-builder-udc.editor :refer [new-editor update-landing-site]]
            ))

(defn start []
  "Application driver that initializes, hooks up event handlers to elements  and starts the application going"
  (do
;;    (new-editor :body)
    ;;    (js/initialize_legacy_editor)
    (.log js/console "Sitebuilder client site starting")
    ))


(start)
;
;            [site-builder-udc.layout-manager :refer [wire-up]]
;           [site-builder-udc.repl :as repl :refer [connect-app]]
