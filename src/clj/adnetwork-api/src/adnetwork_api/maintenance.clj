(ns adnetwork-api.maintenance
    (:use [clj-webdriver.taxi]))

(defprotocol Ad-Network-Maintenance
  "Perform automated  adwords operations for a goggle adwords account"
  (add-user-account [this account-data]
    "Create a new ad words account")
  (fund-account [this spend-data]
    "Fund the account")
  (disable-account [this]
    "Disable the account so no campaigns run")
  (enable-account [this]
    "All account to run campaigns")
  (disable-campaign [this campaign-name]
    "Disable the account so no campaigns run")
  (enable-campaign [this campaign-name]
    "run campaign")
  (create-campaign [this campaign-data]
    "Construct an entire campaign with keywords, adgroups and ads"))

