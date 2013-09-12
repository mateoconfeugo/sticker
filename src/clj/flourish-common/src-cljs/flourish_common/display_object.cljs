(ns flourish-common.display-object
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]]
                   [cljs.core.match.macros :refer [match]]
                   [cljs.core.async.macros  :refer [go]]
                   [enfocus.macros :as em])
  (:require [flourish-common.utils.helpers :refer [event-chan by-id click-chan]]
            [flourish-common.utils.media-query :refer [media-query-transform init-media-query]]
            [cljs.core.async :refer [<! >! chan put! alts!]]
            [enfocus.core :as ef :refer [from read-form]]
            [jayq.core  :as jq :refer [$ text val on prevent remove-class add-class remove]]            
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))

(defn new-error-channel
  [{:keys [] :as args}]
  (chan))

(defn get-channels [display-object]
  [ ec mqc (:css-in display-object)])

(-> (cljs.reader/read-string (slurp edn)

(defn render-display-object [display-object]
  (let [input (:model display-object)
        transform (:handler-fn display-object)]
    (transform input)))

(defn render-display-object-to-channel [display-object & channel]
    (put! (or channel (:html-out display-object)) (render-display-object display-object)))

(defn route-html-to-el [display-object])

(def create-event-channels [dply-obj-cfg]
  (map #(listen (:el %) (:type %) (:handler-fn %)) (-> dply-obj-cfg :event-channel-handler)))

(defn build-display-object
  [{:keys [handler-fn template css menu-html initial-model-data el] :as cfgs}]
  (let [html (atom {:data (slurp template )})
        css (atom {:data (slurp css)})]
  {:el el
   :editor-menu-html (slurp menu-html)
   :sample-html html
   :current-html html
   :sample-css css
   :model (or initial-model-data  nil)
   :current-css css
   :handler-fn (load handler-fn)
   :data-in (chan)
   :data-out (chan)
   :dom-event-channels (create-event-channels cfgs)
   :ec (new-error-channel {})
   :mqc (init-media-query)
   :css-in (chan)
   :html-in (chan)
   :html-out (chan)}))

(defn run [display-object]
  (go (while true
        (let [input-data (<! data-in)]
          (do
            (swap! (:model display-object) update-in [:data] input-data)
            (render-display-object-to-channel (assoc display-object :model data-in))))))
  (go (while true
        (let [html-data (<! html-in)]
          (.log js/console html-data))))
  (go (while true
        (let [html-data (<! html-out)]
          (at ($ (:el display-object) (html-content html-data))))
  (go (while true
        (let [css-data (<! css-in)]
          (.log js/console css-data))))
  (go (while true
        (let [e (<! media-query-channel)]
          (.log js/console e))))
  (go (while true
        (let [[event choosen] (alts! [(:dom-event-channels display-object)])
              dispatch (:channel-dispatcher display-object)]
          (dispatch {:channel choosen :event event :error-channel ec})))))))
