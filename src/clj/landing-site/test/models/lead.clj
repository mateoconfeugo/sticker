(ns models.lead
  (:use [landing-site.models.lead]
        [expectations]
        [korma.core :only [defentity database insert values select where] :as kdb]        
        [korma.db :only [defdb mysql]]))

(def good-test-lead {:full-name "Cal Mee" :email-address "cal@gmail.com" :phone-number "8182546028"})
(def bad-test-lead {:full-name "l" :email-address "calgmail.com" :phone-number "182546028"})

(expect true (empty? (validate-lead good-test-lead)))
(expect false (empty? (validate-lead bad-test-lead)))

(def entry-id (log-lead good-test-lead))
(def retreived-record (first (kdb/select lead_log
                                  (kdb/fields :full_name :email :phone)
                                  (kdb/where {:id (:GENERATED_KEY entry-id)}))))

(expect true (= (:phone-number good-test-lead) (:phone retreived-record)))
