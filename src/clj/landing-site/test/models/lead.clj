(ns models.lead
  ^{:author "Matt Burns"
    :doc "Component level tests relating to storing and reporting on lead data"}
  (:use [landing-site.models.lead]
        [expectations]
        [korma.core :only [defentity database insert values select where] :as kdb]
        [korma.db :only [defdb mysql]]))

(def good-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028"})
(def bad-test-lead {:full-name "l" :email-address "calgmail.com" :phone-number "182546028"})
(def support-group-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028" :postal-code "91377"})
(def support-bad-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028" :postal-code "9137"})

(expect true (empty? (validate-lead good-test-lead)))
(expect false (empty? (validate-lead bad-test-lead)))
(expect true (empty? (validate-support-lead support-group-test-lead)))
(expect false (empty? (validate-support-lead support-bad-test-lead)))

(def entry (log-lead good-test-lead))
(def retreived-record (first (kdb/select lead_log
                                  (kdb/fields :full_name :email :phone)
                                  (kdb/where {:id  (:id entry)}))))

(expect true (= (:phone-number good-test-lead) (:phone retreived-record)))

(def support-entry (log-support-lead support-group-test-lead))
(def retreived-record (first (kdb/select lead_log
                                  (kdb/fields :full_name :email :phone :postal_code)
                                  (kdb/where {:id (:id support-entry)}))))

(expect true (= (:postal-code support-group-test-lead) (str (:postal_code retreived-record))))
