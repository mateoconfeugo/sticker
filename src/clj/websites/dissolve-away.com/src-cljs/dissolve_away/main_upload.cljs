(ns dissolve-away.main-upload
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]]
                   [cljs.core.match.macros :refer [match]]
                   [cljs.core.async.macros  :refer [go]]
                   [enfocus.macros :as em])
  (:require [flourish-common.utils.helpers :refer [event-chan by-id click-chan]]
            [heat-mapping.client :refer [send-coords]]
            [lead-generation.client :refer [send-lead]]
            [cljs.core.async :refer [<! >! chan put! alts!]]
            [clojure.browser.repl :as repl]            
            [enfocus.core :as ef :refer [from read-form]]
            [jayq.core  :as jq :refer [$ text val on prevent remove-class add-class remove]]            
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))




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

(defn ^:export run []
  (let [lead-channel (click-chan "#lead-form-submit" :new-lead)
        mouse-channel (:chan (event-chan js/window "mousemove"))
        media-query-channel (chan)
        media-query-list (.matchMedia  js/window "(max-width: 500px)")
        mq-cb-handler (fn [obj] (if (.-matches media-query-list) (go (>! media-query-channel obj))))
        _ (.addListener media-query-list mq-cb-handler)]

    (go (while true
          (let [e (<! media-query-channel)
                form-el (jq/$ :#top-form)
                _ (jq/remove-class form-el "form-inline")]
;                _ (jq/add-class form-el "form-horizontal")]
            (.log js/console form-el))))
    (go (while true
              (let [e (<! lead-channel)
                name (jq/val (jq/$ :#lead_full_name))
                email (jq/val (jq/$ :#lead_email))
                phone (jq/val (jq/$ :#lead_phone))
                loc (.-location js/window)                
                results (send-lead name email phone)]
            (do
              (set! (.-href loc) (str "/thank-you?name=" name))
              (.log js/console (str name " " email " " phone))))))
    (go  (while true
           (handler (alts! [mouse-channel]))))))


(.log js/console "Starting the Application")
(repl/connect "http://localhost:9000/repl")
(run)


