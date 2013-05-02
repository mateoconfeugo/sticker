(ns adnetwork-api.core
  (:use [clj-webdriver.taxi]
        [adnetwork-api.google :as google]))

(defn setup-campaign-across-adnetworks [adnetworks campaign account]
  "create user accounts if necessary and setup the campaign on google
   and then import it into other networks"
  (doseq [network adnetworks]
    (let [api ((:factory-function network) account)]
      (add-user-account api account)
      (if (= (:adnetwork api) "google")
        (create-campaign api campaign)
        (import-campaign api campaign)))))


(defn create-adnetwork-account [adnetwork]

(defn import-campaign-data [adnetwork campaign])


(defprotocol Ad-Network-Reporting
  "functions related to getting report data from ad networks")




(comment
(if (exists "gwt-uid-921") (click "#gwt-uid-921"))
(click (first (find-elements {:tag :span, :text "Run script"})))
(click "span[gwtdebugid='accept-button-content']")
(click (first (find-elements {:tag :a, :gwtdebugid "aw-scripty-view-details-link"})))
(defn foo []
  (let [elements (find-elements {:tag :span, :class  "aw-scripty-script-log-user"})
        data (atom [])]
    
  (doseq [e elements]
          (swap! data conj (text e)))
  data))

(foo)
)

