(ns payment-gateway.credit-card.processor
  (:require [clj-stripe common cards]))

(defn add-credit-card-to-user-account
  [{:keys [first-name last-name company street-address-1 street-address-2 city
           state-province postal-code country phone credit-card-number expiration-date cvs
           card-contact-email special-notes user-id account-id credit-card-api-token] :as credit-card-data}]  
  (with-token credit-card-api-token
    (let [exp-month (:month expiration-date)
          exp-year (:year expiration-date)
          new-card (card (number credit-card-number) (expiration exp-month exp-year) (cvc cvs) (owner-name (str first-name " " last-name)))
          api-op (create-card-token test-card)
          api-resp (execute api-op)]
      (do (info api-resp)
          api-resp))))
