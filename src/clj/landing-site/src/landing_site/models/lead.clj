(ns landing-site.models.lead
  "storing and manipulating leads"
  (:import [com.google.i18n.phonenumbers PhoneNumberUtil NumberParseException]
           [java.util.zip  DataFormatException])
  (:use [metis.core]
        [korma.core :only [defentity database insert values]]        
        [korma.db :only [defdb mysql]]))

(defdb db (mysql {:db "pcs" :user "root" :password ""} ))
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

(defn log-lead [lead]
  "Store the lead in the database"
  (let [is-valid (empty? (validate-lead lead))
        storable-lead (dissoc (assoc lead
                                :email (:email-address lead)
                                :phone (:phone-number lead)
                                :full_name (:full-name lead)) :email-address :full-name :phone-number)]
    (if is-valid
      (insert lead_log (values [storable-lead]))
      (throw (DataFormatException. "bad lead data")))))
            

