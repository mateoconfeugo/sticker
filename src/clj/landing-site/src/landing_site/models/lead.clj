(ns landing-site.models.lead
  "storing, validating and manipulating leads"
  (:import [com.google.i18n.phonenumbers PhoneNumberUtil NumberParseException]
           [java.util.zip  DataFormatException])
  (:use [metis.core]
        [landing-site.config]
        [korma.db :only [defdb mysql]]
        [korma.core :only [defentity database insert values]]        
        [riemann.client :only [send-event tcp-client]]))        

(defdb db (mysql {:db db-name :user db-user :password db-password :host db-address} ))
(defentity lead_log (database db))

(defn valid-phone-number? [digits]
  (let [phone-util (PhoneNumberUtil/getInstance)
        country-code "US"]
    (try (.isValidNumber phone-util (.parse  phone-util digits country-code))
         (catch NumberParseException e false))))

(defn phone [map key _]
  (when-not (valid-phone-number? (get map key))
    "Invalid phone number"))

(defvalidator  validate-lead
  [[:full-name :email-address :phone-number] :presence]
  [:full-name [:length {:greater-than-or-equal-to 1}]]
  [:email-address :email]
  [:phone-number :phone])

(defvalidator  validate-support-lead
  [[:full-name :email-address :phone-number :postal-code] :presence]
  [:full-name [:length {:greater-than-or-equal-to 1}]]
  [:email-address :email]
  [:phone-number :phone]
  [:postal-code :length {:equal-to 5}])

(defn log-lead [lead]
  "Validate and attempt to store the lead in the database"
  (let [message (validate-lead lead)
        is-valid (empty? message)
        storable-lead (dissoc (assoc lead
                                :email (:email-address lead)
                                :phone (:phone-number lead)
                                :full_name (:full-name lead)) :email-address :full-name :phone-number)]
    (if is-valid
      (do
        (if-let [monitoring-bus (try (tcp-client) (catch IOException))]
          (riemann.client/send-event monitoring-bus {:service "leads" :state "ok" :tags["landing-site"]
                                                     :metric 1 :description "lead form filled out correctly"}))
        {:id (:GENERATED_KEY (insert lead_log (values [storable-lead])))})
      message)))
  
(defn log-support-lead [lead]
  "Store the lead in the database"
  (let [message (validate-lead lead)
        is-valid (empty? message)
        storable-lead (dissoc (assoc lead
                                :email (:email-address lead)
                                :phone (:phone-number lead)
                                :postal_code (:postal-code lead)                                
                                :full_name (:full-name lead)) :email-address :full-name :phone-number :postal-code)]
    (if is-valid
      {:id (:GENERATED_KEY (insert lead_log (values [storable-lead])))}
      message)))
      



