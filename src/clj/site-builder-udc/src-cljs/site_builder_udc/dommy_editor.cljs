(ns site-builder-udc.dommy-editor
  ^{:author "Matthew Burns"
    :doc "Display Object containing the functionality build a web site"}
  (:require-macros [cljs.core.match.macros :refer [match]]
                   [cljs.core.async.macros  :refer [go]]
                   [dommy.macros :refer [node sel sel1 deftemplate]])
                   [shoreleave.remotes.macros :as srm :refer [rpc]])
  (:require [cljs.core.async :refer [alts! chan >!! >! put!]]
            [cljs.core.async.impl.ioc-helpers ]
            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [flourish-common.utils.helpers :refer [click-chan]]
            [jayq.core :refer [$ text val on prevent remove-class add-class remove empty html children append]]
            [shoreleave.browser.storage.sessionstorage :refer [storage]]
            [shoreleave.remote]
            [site-builder-udc.views.editor :as tmpl :refer [html-editor]]))


(def session (storage))
(assoc! session :user-id 1)
(assoc! session :landing-site-id 1)

(defn extract-sb-tuple
  [event ui]
  "Gather the necessary dom and admin data from a droppable event and ui element node"
  (let [demo (html ($ ".demo"))
        dom (nth (js->clj (.-item ui)) 0)
        xpath (js/getXPath dom)]
    {:page-html demo :snippet-html (.-innerHTML dom) :xpath xpath :dom dom
     :landing-site-id (:landing-site-id session) :user-id (:user-id session)}))

(defn drop-channel
  [selector msg-name formatter]
  "Create an element channel that response to drop events on the element by extracting the
   relevent data from the event and ui object and sending it as a message to be dispatched
   via the created channel"
  (let [elm ($ selector)
        element-channel (chan)
        _ (.sortable elm (clj->js {:receive (fn [event ui]
                                              (do (.log js/console (clj->js [event ui elm]))
                                                  (go (>! element-channel [msg-name (assoc (formatter event ui) :target elm)]))
                                                  ))}))]
    element-channel))

(defn update-landing-site
  [{:keys [page-html xpath snippet-html  user-id landing-site-id drop-channel dom target] :as tuple}]
  "Call the server side persiting logic to save the landing site edits send the results
   back to the dispatcher to be handled"
  (srm/rpc
   (api/update {:page-html page-html :xpath xpath :snippet-html snippet-html :user-id user-id :landing-site-id landing-site-id}) [resp]
   (put!  drop-channel [:landing-site-updated (assoc resp :dom dom :target target)])))

(defn save-landing-site
  [{:keys [page-html user-id landing-site-id drop-channel] :as tuple}]
  "Call the server side persiting logic to save the landing site edits"
  (srm/rpc
   (api/save {:page-html page-html :user-id user-id :landing-site-id landing-site-id}) [resp]
   (put! drop-channel [:landing-site-updated {:page-html resp}])))

(defn render-workspace
  [ch model elm]
  "Insert the new html with the recent edits"
  (do (if (:uuid model)
;;        (add-class ($ ((.-getElementsByXPath js/xpath) js/document (:xpath model))) (str "uuid_" (:uuid model))))
        (.log js/console (clj->js model)))
      (.log js/console (js/getXPath (:target model)))
      ;;      (add-class ($ (:dom model)) "demo")
      (add-class ($ elm) "demo")
      (html ($ elm) (or (:tmp-page-html model) (:page-html model)))))

(defn dispatch
  [ch val]
  "Dispatch table that routes channel event data to the correct handler
   val is a vector with a keyword to be dispatched on and the value from the
   channel"
  (match [(nth val 0)]
         [:save-landing-site] (save-landing-site {:page-html (html ($ ".demo")) :user-id (:user-id session)
                                                  :landing-site-id (:landing-site-id session) :drop-channel ch})
         [:drop-snippet] (update-landing-site (assoc (nth val 1) :drop-channel ch))
         [:landing-site-updated] (render-workspace ch (nth val 1) ($ ".demo"))))

(defn render [selector]
  (let [_ (.log js/console (str "selector:  ") selector)
        output-html (tmpl/html-editor {})
        _ (.log js/console (str "output:  ") output-html)
        ]
    (at js/document selector (html-content output-html))
    ))

(defn render2 [selector] (at [selector] (html-content (tmpl/html-editor {}))))

(defn render3 [] (at js/document g["body"] (html-content (tmpl/html-editor {}))))

(defn new-editor
  [selector]
  "Create the event channels and start the loop that feeds the event data to the dispatcher"
  (let [
        ;;        app-html (render selector)
          app-html (render3)
;;        save-channel (click-chan "#save-landing-site" :save-landing-site)
;;        drop-channel (drop-channel ".demo" :drop-snippet extract-sb-tuple)
;        channels [save-channel drop-channel]
        ]
;;    (go (while true
;;    (let [[val ch] (alts! channels)]
;;     (dispatch ch val)    )))
    ))
