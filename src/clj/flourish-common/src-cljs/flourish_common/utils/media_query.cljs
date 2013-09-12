(ns flourish-common.utils.media-query
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]]
                   [cljs.core.async.macros  :refer [go]]
                   [enfocus.macros :as em])
  (:require [flourish-common.utils.helpers :refer [event-chan by-id click-chan]]
            [cljs.core.async :refer [<! >! chan put! alts!]]
            [enfocus.core :as ef :refer [from read-form]]
            [jayq.core  :as jq :refer [$ text val on prevent remove-class add-class remove]]            
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))

(defn media-query-transform
  [media-query-channel transform]
  (let [e (<! media-query-channel)
        form-el (jq/$ :#top-form)
        _ (jq/remove-class form-el "form-inline")]
;;      _ (jq/add-class form-el "form-horizontal")]
    (.log js/console form-el))))

(defn init-media-query []
  (let [media-query-channel (chan)
        media-query-list (.matchMedia  js/window "(max-width: 500px)")
        mq-cb-handler (fn [obj] (if (.-matches media-query-list) (go (>! media-query-channel {:rule "foo"}))))
        _ (.addListener media-query-list mq-cb-handler)]
    media-query-channel))
