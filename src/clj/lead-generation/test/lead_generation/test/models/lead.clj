(ns lead-generation.test.models.lead
  (:require [lead-generation.models.lead :refer [select-database log-lead validate-lead validate-support-lead]]
            [expectations :refer [expect]]
            [korma.core  :as kdb :refer [defentity database insert values select where]]        
            [korma.db :refer [defdb mysql]]))

(def good-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028"})
(def bad-test-lead {:full-name "l" :email-address "calgmail.com" :phone-number "182546028"})
(def support-group-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028" :postal-code "91377"})
(def support-bad-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028" :postal-code "9137"})

(expect true (empty? (validate-lead good-test-lead)))
(expect false (empty? (validate-lead bad-test-lead)))
(expect true (empty? (validate-support-lead support-group-test-lead)))
(expect false (empty? (validate-support-lead support-bad-test-lead)))

(def dbh (select-database "pcs" "root" "test123" "127.0.0.1"))

(def entry (log-lead dbh good-test-lead))
(def retreived-record (first (kdb/select "lead_log"
                                  (kdb/fields :full_name :email :phone)
                                  (kdb/where {:id  (:id entry)}))))

(expect true (= (:phone-number good-test-lead) (:phone retreived-record)))

(def support-entry (log-support-lead support-group-test-lead))
(def retreived-record (first (kdb/select lead_log
                                  (kdb/fields :full_name :email :phone :postal_code)
                                  (kdb/where {:id (:id support-entry)}))))

(expect true (= (:postal-code support-group-test-lead) (str (:postal_code retreived-record))))
