(ns adnetwork-api.google
  (:use [clj-webdriver.taxi :as taxi :only(to click exists? input-text submit)]))
       

(defn navigate-to-adwords-scripts
  [{:keys [site-name driver]}]
  "guide the browser to the scripts area of the interface"
  (click driver (first (find-elements {:tag :a, :text site-name})))
  (click driver (first (find-elements {:tag :a, :text "Bulk operations"})))
  (click driver (first (find-elements {:tag :a, :text "Scripts"}))))

(defn run-existing-script
  [{:keys [script-name driver]}]
  "run an adwords script"
  (click driver (first (find-elements {:tag :span, :text "Run"}))))

(defn load-new-script
  [{:keys [script-name code driver] :as options}]
  "loads a script into the ad words scripts list")

(defn login 
  [{:keys [username password driver] :as options}]
  (taxi/to  "http://adwords.google.com")
  (taxi/click  "a[data-g-label*='Sign In']")
  (taxi/exists?  "#Email")
  (taxi/exists?  "#Passwd")
  (taxi/input-text  "#Email" username)
  (taxi/input-text  "#Passwd" password)
  (taxi/submit "#Passwd"))

(defn run-adwords-script 
  [{:keys[site-name browser username password script-name driver] :as options}]
  "run a particular script and return the results if any"
      (login options)
      (navigate-to-adwords-scripts options)
      (run-existing-script options))

(def driver (new-driver {:browser :firefox}))
;;(set-driver! driver)
(def args {:site-name "Patient Comfort Referral - Initial" :username "jobs4matt@hotmail.com" :password "Pickle12" :browser "firefox" :driver driver})
(login args)
(def ham (run-adwords-script args))


(comment
(defprotocol Foo
  (run-adwords-script [this script-name]
    "run a particular script and return the results if any")
  (load-adwords-script [this code]
    "loads and ad words script"))
  
  
      Ad-Network-Maintenance

      (add-user-account [this account-data])

      (fund-account [this spend-data]
        (run-script this "FundAccount"))

      (disable-account [this])
      (enable-account [this])
      (disable-campaign [this campaign-name])
      (enable-campaign [this campaign-name])
      (create-campaign [this campaign-data]))      

