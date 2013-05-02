(ns landing-site.models.lead
  (:require [clojure.java.jdbc :as sql])
  (:use [metis.core])
  (:import [com.google.i18n.phonenumbers :as phone]))

(import 'com.google.i18n.phonenumbers)

(defdb db (mysql {:db lead-db :user "root" :password ""}))

(defentity lead_log (database db))

(defn valid-phone [phone]
  (let [phone-util (phone.PhoneNumberUtil/getInstance)
        phone-number (.parse phone-util)]))

(defvalidator lead-validator
  [[:full-name :phone-number :email-address] :presence]
  [:full-name :with {:validator (fn [attr] false) :message "error!"}]
  [:email-address :email]
  [:phone-number {:validator valid-phone}])

(defn log-lead [lead]
  "Store the lead in the database"
  (insert lead_log (values [lead]))
