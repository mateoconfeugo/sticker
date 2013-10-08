(ns payment-gateway.credit-card
  (:require-macros [cljs.core.match.macros :refer [match]]
                   [cljs.core.async.macros  :refer [go]]
                   [shoreleave.remotes.macros :as srm :refer [rpc]])
  (:require [cljs.core.async :refer [alts! chan >!! >! put!]]
            [flourish-common.utils.helpers :refer [click-chan]]
            [jayq.core :refer [$ text val on prevent remove-class add-class remove empty html children append]]
            [shoreleave.browser.storage.sessionstorage :refer [storage]]
            [shoreleave.remote]))

(defn gather-form-data []
  "Retreive the data form the dom form"
  {:first-name nil
   :last-name nil
   :company nil
   :street-address-1 nil
   :street-address-2 nil
   :city nil
   :state-province nil
   :postal-code nil
   :country nil
   :phone nill
   :credit-card-number nil
   :expiration-date nil
   :card-contact-email nil
   :special-notes nil})

(defn add-credit-card
  [{:keys [user-id  credit-card-channel] :as tuple}]  
  "Call the server side credit card logic to add a credit card to a users account"
  (let [card-data (gather-form-data )]
  (srm/rpc
   (api/add-credit-card card-data) [resp]
   (if (= :success (:status response))
     (put! (chan) [:add-card-approved resp])
     (put! (chan) [:add-card-rejected resp]))))

(defn add-card-approved [])
(defn add-card-rejected [])

(defn dispatch
  [ch val]
  "Dispatch table that routes channel event data to the correct handler
   val is a vector with a keyword to be dispatched on and the value from the
   channel"
  (match [(nth val 0)]
         [:add-card] (add-credit-card)
         [:add-card-approved] (add-card-approved (:redirect val))
         [:add-card-rejected] (add-card-rejected (:redirect val))))

(defn new-credit-card-form
  []
  "Create the event channels and start the loop that feeds the event data to the dispatcher"
  (let [creditcard-channel (chan)
        submit-info-channel (click-chan "#submit-creditcard-form" :add-card)	
        channels [save-channel drop-channel]]
    (go (while true
        (let [[val ch] (alts! channels)]
          (dispatch ch val))))))

