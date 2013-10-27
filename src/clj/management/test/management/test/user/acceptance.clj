(ns management.test.user.acceptance
  ^{:author "Matthew Burns"
    :doc "Acceptance test revolving around proving a visitor can become a user signup with stripe, github, heroku,  access the system
          deploy application to heroku and have the app direct the user to their landing site marketing application running on heroku.
          The target hosting the system can be the localhost, virtual box, QA in the cloud and production
          in the clould"}
  (:require [clojure.core]
            [clojure-mail.core :refer [inbox read-message]]
            [clojure.edn]
            [clojure-mail.message :refer [message-headers from subject]]
            [clojure.pprint :as pp :refer [pprint]]
            [clj-webdriver.taxi :as scraper :refer [set-driver! to click exists? input-text submit quit]]
            [clj-stripe.common :refer [plan description trial-end coupon with-token execute card number cvc expiration owner-name]]
            [clj-stripe.customers :refer [email create-customer get-customer delete-customer]]
            [expectations :refer [expect]]
            [tentacles.repos :refer [create-repo]]
            [tentacles.core :refer [api-call]]
            [heroku-clj.core :as heroku :refer [app-create set-api-key! app request do-request <<< with-key]]
            [korma.core :as kdb :refer [defentity database insert values has-many many-to-many
                                        transform belongs-to has-one fields table prepare pk
                                        subselect where belongs-to limit aggregate order]]
            ;;
            [management.dev-ops-config :refer [db-settings management-settings]]
            [management.groups.database :refer [db-group-spec qa-mgmt-db]]
            [management.models.orm-spec :refer [user]]
            [management.handler :as mgmt :refer [start]]
            [management.users :refer [create-stripe-account create-heroku-app deploy-heroku-app new-user-signup]]
            [postal.core :refer [send-message]]
;;            [postmark.core :as pm :refer [postmark send-message]]
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
(def test-port 3000)
(def test-user {:email "marketingwithgusto@gmail.com" :password "test123"})
(def signup-uri "/")
(def login-uri "/login")
;; (converge (db-group-spec 0) :compute service)

(def test-credentials (clojure.edn/read-string (slurp (format "%s/test/testinfo.edn" (System/getProperty "user.dir")))))
(def test-password (:password test-credentials))
(def test-username (:username test-credentials))
(def auth (format "%s:%s"  test-username test-password))

(def test-app-server (mgmt/start mgmt/app))
(.stop test-app-server)
;;(when (= "localhost") (mgmt/start 8087))
;;========================================================================
;; ACCEPTANCE TEST: User Signup
;;========================================================================
;; Register user save in database navigate to wizard page
;; Start up a browser and go to user sign-up
(def app-login-uri (str "http://" management-ip ":" test-port login-uri))
(def app-signup-uri (str "http://" management-ip ":" test-port signup-uri))
(scraper/set-driver! {:browser :firefox} app-signup-uri)

;; input sign up info
(scraper/to app-signup-uri)
(scraper/input-text "#username" (:email test-user))
(scraper/input-text "#password" (:password test-user))
(scraper/input-text "#confirm_password" (:password test-user))
(scraper/click "button.button.blue.medium")

;;========================================================================
;; COMPONENT TEST: User persisted in system?
;;========================================================================
(def user-record (first (kdb/select user (kdb/fields :id :moniker) (kdb/where {:moniker (:email test-user)}))))
(expect true (= (:email test-user) (:moniker user-record)))

;; First time loging in so role out the welcome
;;(def expected-welcome-modal "/user/welcome.html")
;; Development has gotten to here
;;========================================================================
;; COMPONENT TEST: New user added to stripe system
;;========================================================================
(def stripe-api-test-token "sk_test_XkUBfdyjFNdh4EHa6D6Vk5nn")
(def test-card (card (number "4242424242424242") (expiration 12 2013) (cvc 123) (owner-name "Mr. Owner")))
(def test-stripe-settings {:user-email (:email test-user)
                           :card test-card
                           :description-info "test new user automated provisioning"
                           :payment-gateway-token stripe-api-test-token})

(def test-customer-result (create-stripe-account test-stripe-settings))
(expect true (= (:email test-user) (:email test-customer-result)))

(with-token (str stripe-api-test-token ":")
  ;; REMOVE stripe test account
  (def test-op  (delete-customer (:id test-customer-result)))
  (def test-customer-result (execute test-op)))

;;========================================================================
;; COMPONENT TEST: New git repo for the user-account combination that
;; be responsible for performing version control on published assets
;; published from the authoring process
;;========================================================================
(def git-api-token "e5d18ca7924a2368e4f973bd30029862c3921907")
(def test-git-settings {:user-id 1
                        :api-token git-api-token
                        :username "tester"
                        :domain "marketwithgusto.com"
                        :decription-info "test repo for test landing site"})

(def test-homepage (format "%s-%s-%s"  "marketingwithgusto.com" 1 "test-1"))
(def test-git-repo-result (create-repo test-homepage {:oauth-token git-api-token
                                                      :description "test repo"
                                                      :public true
                                                      :has-issues true :has-wiki true :has-downloads true}))


;;(def del-git-repo-results (api-call :delete "repos/%s/%s"  ["mateoconfeugo" "marketingwithgusto.com-1-test-1"]  {:oauth-token git-api-token
;;                                                                                                               :scopes ["delete_repo"]}))
(def del-git-repo-results (api-call :delete "repos/%s/%s"  ["mateoconfeugo" "marketingwithgusto.com-1-test-1"]  {:auth auth}))

;;========================================================================
;; COMPONENT TEST: New web app added for default landing site for the
;; initial user account combination that  will serve the landing sites
;; for the domains associated with the fore-mentioned user account combination
;;========================================================================
(def heroku-api-token  "5aef21fa-3f44-469b-b22a-62bfad6909ed")
(def test-heroku-app-settings {:app-host-api-key heroku-api-token
                               :account-id 1
                               :username (:email test-user)})

(heroku/app heroku-api-token)
(heroku/set-api-key! heroku-api-token)
(heroku/app-create heroku-api-token "bob-1")
(app-create heroku-api-token )
(def test-heroku-create-app-resp  (heroku/<<< :post
                                              "https://api.heroku.com/apps"
                                              heroku-api-token
                                              {:name "test-one"}))

;; :region {:id "01234567-89ab-cdef-0123-456789abcdef" :name "us"} :stack "cedar"

@test-heroku-create-app-resp


(create-heroku-app test-heroku-app-settings)
(def test-app (app heroku-api-token "bob-1"))
(nth (app heroku-api-token) 1)

(app heroku-api-token "management")



;;========================================================================
;; COMPONENT TEST: Heroku application default application deployed into
;; the cloud
;;========================================================================
(deploy-heroku-app )

;;========================================================================
;; UNIT TEST: Create test promotional offer to include in welecome email
;;========================================================================
(prepare-promotional-offer)
;;========================================================================
;; COMPONENT TEST: New users gets a welcome email
;;========================================================================

(send-message ^{:host  "smtp.postmarkapp.com"
                :user "609a1dce-8d69-4fd0-97b2-3645c2e43f7a"
                :pass "609a1dce-8d69-4fd0-97b2-3645c2e43f7a"}
              {:from "matthewburns@gmail.com"
               :to (:email test-user)
               :subject "Hi!"
               :body "Test."})

(def api-key "609a1dce-8d69-4fd0-97b2-3645c2e43f7a")
(send-welcome-email)
;;========================================================================
;; ACCEPTANCE TEST: User redirect to right place
;;========================================================================
(def expected-url "http://127.0.0.1:3000/mgmt/user/1")
(expect true  (= (scraper/current-url) expected-url))

;; User gets an activation email and navigates to the link in the email that activates the account
;;(def mail-inbox (inbox "marketingwithgusto@gmail.com" "test123123987^%&!"))
;;(def message (read-message (last  mail-inbox)))
;;(def confirmation-email-subject (:subject message))
;;(def confirmation-email-body (:subject message))
;;(def confirmation-url nil)

;; Users status is set to initial-signup-prohibiation and activated when the user clicks on the link provided in email
;;(scraper/to confirmation-url)

;;========================================================================
;; ACCEPTANCE TEST: User can log out
;;========================================================================
(scraper/to "http://127.0.0.1:3000/logout")
(expect true  (= (scraper/current-url) "http://127.0.0.1:3000/logout"))

;;========================================================================
;; ACCEPTANCE TEST: User can log in via the normal log in screen and end up on the user_dashboard
;;========================================================================
(scraper/to "http://127.0.0.1:3000/login")
(scraper/input-text "#username" (:email test-user) )
(scraper/input-text "#password" (:password test-user))
(scraper/click "#submit-login")
(expect true  (= (scraper/current-url) "http://127.0.0.1:3000/mgmt/user/1"))
;; Clean up
;;(kdb/delete user (kdb/where {:username (:username test-user)}))
;;(power-down lsbs-provider-host)
;;(destroy lsbs-provider-host)
(.stop test-app-server)

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

  [{:keys [user-email username password prefix git-username git-password  git-api-token
           smtp-host email-saas-api-token  welcome-email-address welcome-subject description domain] :as settings}]

(def new-user-test-settings {:user-email (:email test-user)
                             :username "ham"
                             :password (:password test-user)
                             :git-username test-username
                             :git-password test-password
                             :git-api-token "e5d18ca7924a2368e4f973bd30029862c3921907"
                             :payment-gateway-token stripe-api-test-token
                             :smtp-host "smtp.postmarkapp.com"
                             :email-saas-api-token "609a1dce-8d69-4fd0-97b2-3645c2e43f7a"
                             :welcome-email-address "matthewburns@gmail.com"
                             :welcome-subject "Welcome to Gusto"
                             :description "adding a test user into the system"
                             :domain "marketingwithgusto.com"})


(new-user-signup new-user-test-settings)

(api-call :delete "repos/%s/%s"  ["mateoconfeugo" "marketingwithgusto.com-1-test-1"]  {:auth auth})
(with-token (str stripe-api-test-token ":")
  ;; REMOVE stripe test account
  (def test-op  (delete-customer (:id test-customer-result)))
  (def test-customer-result (execute test-op)))
