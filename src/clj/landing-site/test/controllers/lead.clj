(ns controllers.lead
  (:use [ring.mock.request]
        [landing-site.handler :only(app)]
        [landing-site.controllers.lead]
        [landing-site.models.lead]        
        [cheshire.core :only (parse-string)]
        [korma.core :only [defentity database insert values select where] :as kdb]
        [expectations]))

;;(def test-lead {"lead_email" "matthewburns@gmail.com" "lead_full_name" "bob smit" "lead_phone" "8182546028"})
;;(def test-lead {:email-address "matthewburns@gmail.com" :full_name "bob smit" :phone-number "8182546028"})
(def test-lead {:lead_email "matthewburns@gmail.com" :lead_full_name "bob smit" :lead_phone "8182546028"})

(expect true (= (:status  (app (body (request :post  "/lead") test-lead))) 200))

