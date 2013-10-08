(ns management.test.user.acceptance
  ^{:author "Matthew Burns"
    :doc "Acceptance test revolving around proving a visitor can become a user and  access the system.
          The target hosting the system can be the localhost, virtual box, QA in the cloud and production
          in the clould"}
  (:require [clojure.core]
            [clojure-mail.core :refer [inbox read-message]]
            [clojure-mail.message :refer [message-headers from subject]]
            [clj-webdriver.taxi :as scraper :refer [set-driver! to click exists? input-text submit quit]]
            [expectations :refer [expect]]
            [management.dev-ops-config :refer [db-settings management-settings]]
            [management.groups.database :refer [db-group-spec qa-mgmt-db]]
            [management.handler :as mgmt :refer [start]]
            [pallet.action :refer [with-action-options]]
            [pallet.configure :refer [compute-service defpallet compute-service-from-config]]
            [pallet.compute.node-list :refer [make-localhost-node]]
            [pallet.compute.localhost :refer [->LocalhostService]]
            [pallet.api :refer [converge lift server-spec group-spec plan-fn]]
            [pallet.actions :refer [remote-file exec-script*]]))

;;            [management.servers.database :refer [copy-schema load-schema]]


(def schema-path "/Users/matthewburns/github/florish-online/src/clj/management/resources/mgmt.sql")
(def load-cmd (format "mysql  -u %s -p%s mysql < %s" "root" "test123" schema-path))
(def load-schema (server-spec :phases {:configure (plan-fn (exec-script* load-cmd))}))

;; Spin up a system of virtual machines to test with
;; (def service (compute-service (:provider db-settings)))
(def mgmt-db-service-spec-localhost (server-spec :extends [load-schema]))
(def mgmt-db-group-spec-localhost (group-spec  "flourish-mgmt" :extends [mgmt-db-service-spec-localhost]))
(def mgmt-pallet-localhost  (defpallet
                              :admin-user  {:username "matthewburns"
                                            :private-key-path "~/.ssh/id_rsa"
                                            :public-key-path "~/.ssh/id_rsa.pub"}
                              :services  {:data-center {:provider "localhost"
                                                        :environment
                                                        {:user {:username "matthewburns"
                                                                :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                                                :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                                                        :node-list [make-localhost-node]}}))

;;(def deployment-target) (cond)
(def mgmt-service-localhost (pallet.configure/compute-service-from-config mgmt-pallet-localhost :data-center {}))
;;(def service (compute-service  "node-list" :node-list [(make-localhost-node)]))
;;(def management-nodes (future (converge {mgmt-db-group-spec-localhost 1} :compute mgmt-service-localhost)))
(def management-nodes (future (converge  {mgmt-db-group-spec-localhost 1}  :compute mgmt-service-localhost)))

;;(def management-nodes (converge {database-group 1} :compute service))
(def management-node  ((first (@management-nodes :targets)) :node))
(def management-ip (.primary-ip management-node))
(def test-port 8087)
(def test-user {:email "marketingwithgusto@gmail.com" :password "test123"})
(def signup-uri "/signup")
(def login-uri "/login")
;; (converge (db-group-spec 0) :compute service)

(mgmt/start 8087)
;;(when (= "localhost") (mgmt/start 8087))
;; ACCEPTANCE TEST: User Signup
;; Register user save in database navigate to wizard page
;; Start up a browser and go to user sign-up
(def app-login-uri (str "http://" management-ip ":" test-port login-uri))
(def app-signup-uri (str "http://" management-ip ":" test-port signup-uri))
(scraper/set-driver! {:browser :firefox} app-uri)

;; input sign up info
(scraper/to app-signup-uri)
(scraper/input-text "#email" (:email test-user))
(scraper/input-text "#password" (:password test-user))
(scraper/input-text "#confirm_password" (:password test-user))
(scraper/click "button.button.blue.medium")   ;; <-----  DEVELOPMENT Has progessed to here

;; User redirect to right place
(def expected-url "user_dashboard.html")
(expect true  (= (scraper/current-url) expected-url))

;; User persisted in system?
(def user-record (first (kdb/select user (kdb/fields :id :username :email) (kdb/where {:username (:username test-user)}))))
(expect true (= (:username test-user) (:username user-record)))
(expect true (= (current-url) ))

;; First time loging in so role out the welcome
(def expected-welcome-modal "/user/welcome.html")

;; User gets an activation email and navigates to the link in the email that activates the account
(def mail-inbox (inbox "marketingwithgusto@gmail.com" "test123123987^%&!"))
(def message (read-message (last  mail-inbox)))
(def confirmation-email-subject (:subject message))
(def confirmation-email-body (:subject message))
(def confirmation-url nil)

;; Users status is set to initial-signup-prohibiation and activated when the user clicks on the link provided in email
(scraper/to confirmation-url)

;; User can log out
(scraper/to "/logout")

;; User can log in via the normal log in screen and end up on the user_dashboard
(scraper/to "/login")
(scraper/input-text "#username" (:username test-user) )
(scraper/input-text "#password" (:password test-user))
(scraper/click "#login-btn")

;; Clean up
;;(kdb/delete user (kdb/where {:username (:username test-user)}))
;;(power-down lsbs-provider-host)
;;(destroy lsbs-provider-host)

;; (.stop app-server)

;; USER ADMIN
;;(deftest administrator-can-create-operator false)
;;(deftest operator-can-create-network-operator false)
;;(deftest network-operator-can-create-landing-site-operator false)
;;(deftest landing-site-operator-can-create-content-editor false)
;;(deftest landing-site-operator-can-create-content-contributor false)
;;(deftest landing-site-operator-can-create-web-designer false)
;;(deftest landing-site-operator-can-create-site-admin false)
;;(deftest administrator-can-assign-features-to-roles false)
;;(deftest administrator-can-add-features false)
;;(deftest user-change-change-password false)
