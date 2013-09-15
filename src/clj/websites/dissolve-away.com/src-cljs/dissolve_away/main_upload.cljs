(ns dissolve-away.main-upload
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]]
                   [cljs.core.match.macros :refer [match]]
                   [cljs.core.async.macros  :refer [go]]
                   [enfocus.macros :as em :refer [defsnippet deftemplate]])
  (:require [flourish-common.utils.helpers :refer [event-chan by-id click-chan listen]]
            [flourish-common.utils.media-query :refer [media-query-transform init-media-query]]
            [dissolve-away.display-objects.lead :refer [dispatch wireup]]
            [lead-generation.client :refer [send-lead]]
            [cljs.core.async :refer [<! >! chan put! alts! >!! <!!]]
            [clojure.browser.repl :as repl]            
            [enfocus.core :as ef :refer [from read-form content html-content at]]
            [jayq.core  :as jq :refer [$ text val on prevent remove-class add-class remove]]            
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))

;;(pback/cljs-repl :repl-env (doto (repl/repl-env :port 9000) (repl/-setup)))
(doto (repl/repl-env :port 9000) (repl/-setup))

(defn ^:export get-phone-number [id]
  (srm/rpc
   (api/regional-phone-number id)
   [resp]
   (.log js/console (:phone resp))))

;;         [{"x" x "y" y}] (send-coords x y)
(defn ^:export handler [[e c]]
  (match [e]
         [{"x" x "y" y}] nil
         :else nil))

(defn ^:export new-error-channel
  [{:keys [] :as args}]
  (chan))

(defn ^:export get-channels [display-object]
  [(:ec display-object) (:mqc display-object) (:css-in display-object)])

(defn ^:export  render-display-object [display-object]
  (let [model (:model display-object)
        dispatch (:dispatcher display-object)]
    (dispatch  model (:ec display-object))))

(defn ^:export render-display-object-to-channel  [display-object channel]
  (go (>! channel (render-display-object display-object))))

(defn ^:export route-html-to-el [display-object])

(defn  create-event-channels [dply-obj-cfg]
  (map #(listen (:el %) (:type %) (:handler-fn %)) (-> dply-obj-cfg :event-channel-handler)))

(defn ^:export build-display-object
  [{:keys [name type dispatcher template css menu-html menu-item-html el initial-model-data] :as cfgs}]
  {:name name
   :type type
   :el el
   :constructor wireup
   :event-channel-handler []
   :editor-menu-html menu-html
   :sample-html template
   :current-html template
   :sample-css css
   :model (or initial-model-data  {})
   :current-css css
   :dispatcher dispatch
   :data-in (chan)
   :data-out (chan)
   :dom-event-channels (create-event-channels cfgs)
   :ec (new-error-channel {})
   :mqc (init-media-query)
   :css-in (chan)
   :html-in (chan)
   :html-out (chan)})

(defn ^:export run [dis-obj]
  (do
    ;;    ((:constructor dis-obj) (chan) (:ec dis-obj))
    (.log js/console (str "setting up the " (:name dis-obj)))
    (go (while true
          (let [data (<! (:data-in dis-obj))
                ch (:html-out dis-obj)]
            (render-display-object-to-channel (assoc dis-obj :model data ) ch))))
    (go (while true
          (let [html-data (<! (:html-out dis-obj))]
            (do
              (.log js/console html-data)
              (ef/at (jq/$ (:el dis-obj)) (ef/html-content html-data))))))))

(repl/connect "http://localhost:9000/repl")

(def test-display-object (build-display-object {:el "#top-form"
                                                :menu-html "<div><form><input type='text'></form</div>"
                                                :menu-item-html "<h1>Lead Generator Form</h1>"
                                                :template "<form class='lead'><input id='phone' type='text'><input id='name' type='text'></form>"
                                                :css ".lead { color: red;}"
                                                :name "test-lead-gen-form"
                                                :type "lead-gen-form"
                                                :handler-fn lead-dom-event-handler}))

(.log js/console test-display-object)
(.log js/console "Starting the Application")
(run test-display-object)
(go (>! (:data-in test-display-object) "Blah"))

