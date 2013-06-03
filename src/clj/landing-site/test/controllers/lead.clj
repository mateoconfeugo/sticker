(ns controllers.lead
  "Purpose: Test the lead generation input system.  First we run some unit tests
   We use the lead stored in the database later in the acceptance tests.  A web browsers
   is used to navigate to a running instance of the server input the data and then see if
   the lead gets stored in the database.  Then incorrect data is put into the system to see
   how it handles validation errors.  The same tests are then run against a support lead"
  (:use [ring.mock.request]
        [landing-site.handler :only(app start)]
        [landing-site.controllers.lead]
        [landing-site.models.lead]        
        [cheshire.core :only (parse-string generate-string)]
        [korma.core :only [defentity database insert values select where] :as kdb]
        [expectations]
        [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]))

;; UNIT TESTS
(def test-lead (generate-string {:email "matthewburns@gmail.com" :full_name "bob smit" :phone "8182546028"}))
(def test-response (app (body (request :post  "/lead") test-lead)))
(def retreived-record (first (kdb/select lead_log
                                  (kdb/fields :full_name :email :phone :postal_code)
                                  (kdb/where {:id (:id test-response)}))))
(expect true (= (:status test-response) 200))
(expect true (= (:full_name test-lead) (:full_name retreived-record)))

;; ACCEPTANCE TESTS
(def test-port 8087)
(def test-uri "/adnetwork/1/campaign/1/adgroup/1/listing/A/market_vector/1/view")

(def app-server (start 8087))
;; Start up a browser
(scraper/set-driver! {:browser :firefox})
;; input lead info
(scraper/to (str "http://localhost:" test-port test-uri))
(scraper/input-text "#lead_full_name" "test matt")
(scraper/input-text "#lead_phone" "8182546027")
(scraper/input-text "#lead_email" "matthewburns@gmail.com")
(scraper/click "#lead-form-submit")
(scraper/quit)
(.stop app-server)
(def prev-record-id (:id (parse-string (:body test-response) true)))
(def current-id (inc prev-record-id))
(def db-record (first (kdb/select lead_log
                                  (kdb/fields :id :full_name :email :phone :postal_code)
                                  (kdb/where {:id current-id}))))
(expect true (= "test matt" (:full_name db-record)))

;; CLEANUP
(kdb/delete lead_log
            (kdb/where {:id prev-record-id}))
(kdb/delete lead_log
            (kdb/where {:id (:id db-record)}))


