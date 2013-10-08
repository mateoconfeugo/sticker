(ns business-rule-service.core
  (:require [clojure.core.async :refer [chan]]
            [clojure.core.match :refer [match]]))


(defn handle-business-exception
  [exception channel]
  (match [(nth exception 0)]
         [:insufficient-funds] nil
         [:insufficient-budget] nil
         [:number-landing-site-limit] nil  ))       
         

(defn business-event-rule-dispatcher
  [ch val]
  "Dispatch table that routes channel business event data to the correct handler"
  (match [(nth val 0)]
         [:publish-landing-site] (check customer-standing)
         [:busniness-exception] (handle-business-exception val ch)
         [:landing-site-updated] (render-workspace ch (nth val 1) ($ ".demo"))))

(defn connect
  [output-channel]
  (let [rule-channel (chan)]
    (go (while true
          (let [[val ch] (alts! channels)]
            (dispatch ch val))))
    rule-channel))

(defn publish
  [{:keys [] :as args}]
  (let [response-channel (chan)
        biz-rule-input-channel (business-rule-service.core/connect response-channel]
        result (>!! biz-rule-input-channel args)]
    (do
      (if (= :failed (:status result))
        (handle-business-exception result)
        (publish-landing-site args)))
    (go (
        
  

