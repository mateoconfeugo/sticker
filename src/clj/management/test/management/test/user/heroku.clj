(ns management.test.user.heroku
  ^{:author "Matthew Burns"
    :doc "Acceptance test revolving around proving a visitor can become a user signup with stripe, github, heroku,  access the system
          deploy application to heroku and have the app direct the user to their landing site marketing application running on heroku.
          The target hosting the system can be the localhost, virtual box, QA in the cloud and production
          in the clould"}

  (:require [clojure.core]
            [clojure-mail.core :refer [inbox read-message]]
            [clojure.edn]
            [cheshire.core :refer [generate-string]]
            [clojure-mail.message :refer [message-headers from subject]]
            [clojure.pprint :as pp :refer [pprint]]
            [clj-webdriver.taxi :as scraper :refer [set-driver! to click exists? input-text submit quit]]
            [clj-stripe.common :refer [plan description trial-end coupon with-token execute card number cvc expiration owner-name]]
            [clj-stripe.customers :refer [email create-customer get-customer delete-customer]]
            [org.httpkit.client :as http]
            [expectations :refer [expect]]
            [tentacles.repos :refer [create-repo]]
            [tentacles.core :refer [api-call]]
;;            [heroku-clj.core :as heroku :refer [app-create set-api-key! app request do-request <<< with-key]]
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
            [pallet.actions :refer [remote-file exec-script*]])
  (:import  [com.heroku.api HerokuAPI]
            [com.heroku.api.App ]
            [com.heroku.api.request.app AppDestroy ]
            [com.heroku.api Heroku$Stack]
            [com.heroku.api.connection Connection]
            [com.heroku.api.connection ConnectionFactory]))

(defn test-remove-heroku-app
  [name api-token]
  (let [conn (ConnectionFactory/get)]
    (.execute conn (AppDestroy. name) api-token)))

(defn test-create-heroku-app
  "Create a new heroku application returns"
  [name api]
  (let [conn (ConnectionFactory/get)
        app (-> (App. )
                (.named name)
                (.on com.heroku.api.Heroku$Stack/Cedar))]
    (.createApp api app)))


;;            [management.servers.database :refer [copy-schema load-schema]]
;;========================================================================
;; COMPONENT TEST: New web app added for default landing site for the
;; initial user account combination that  will serve the landing sites
;; for the domains associated with the fore-mentioned user account combination
;;========================================================================
(def heroku-api-token  "5aef21fa-3f44-469b-b22a-62bfad6909ed")
(def test-heroku-app-settings {:app-host-api-key heroku-api-token
                               :account-id 1
                               :username (:email test-user)})

(def test-heroku-api (HerokuAPI. heroku-api-token))
(.destroyApp test-heroku-api "boiling-meadow-8886")

(def the-app (.createApp test-heroku-api test-app ))
(def conn (ConnectionFactory/get))
(.execute conn (AppDestroy. "bingo-2013-10") heroku-api-token))
(def test-app  (.named test-app "bingo"))
(.getName test-app)



(test-create-heroku-app "boiling-meadow-8886" test-heroku-api)
(test-remove-heroku-app "boiling-meadow-8886" heroku-api-token)

(-> (AppDestroy. "bingo-2013-10")
    (.getResponse))

(.createApp test-heroku-api test-app)

(.git_url test-app)
(set! (.git_url test-app)  "turkey")

(com.heroku.api.App/named "blah")
(def test-app (App.))
(.getId test-app )
(.setName test-app "wha")
(Keys (.listConfig test-heroku-api "management"))

(.name test-app)

(def apps-list (.listApps test-heroku-api))
(.named test-app "mega-ham")

(def cb )

@(http/request    {:method :post
                   :url "https://api.heroku.com/apps"
                   :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                             "Accept" "application/vnd.heroku+json; version=3"}
                   :body (generate-string {"name" "blah92329ujdkajfdsef" "git-url" "https://github.com/mateoconfeugo/marketingwithgusto.com-1-test-1.git"})}
                  (fn [{:keys [opts status body headers error] :as resp}]
                    (if error  [1 resp] [ 0 resp]))
                  )

@(http/request    {:method :patch
                   :url "https://api.heroku.com/apps/blah92329ujdkajfd"
                   :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                             "Accept" "application/vnd.heroku+json; version=3"}
                   :body (generate-string {"git_url" "https://github.com/mateoconfeugo/marketingwithgusto.com-1-test-1.git"})}
                  (fn [{:keys [opts status body headers error] :as resp}]
                    (if error  [1 resp] [ 0 resp])))

@(http/request    {:method :patch
                   :url "https://api.heroku.com/apps/blah92329ujdkajfd"
                   :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                             "Accept" "application/vnd.heroku+json; version=3"}
                   :body (generate-string {:name "test-1989"})}
                  (fn [{:keys [opts status body headers error] :as resp}]
                    (if error  [1 resp] [ 0 resp])))


(def feature-list @(http/request    {:method :get
                                    :url "https://api.heroku.com/apps/blah92329ujdkajfd/features"
                                    :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                                              "Accept" "application/vnd.heroku+json; version=3"}}
                                   (fn [{:keys [opts status body headers error] :as resp}]
                                     (if error  nil resp))))

(map :name (:body (cheshire.core/parse-string feature-list true)))
(map :name (cheshire.core/parse-string  (:body feature-list) true))

GET /apps/{app_id_or_name}/features


(defn delete-heroku-app [name])
@(http/request    {:method :delete
                   :url (format  "https://api.heroku.com/apps/%s" name)

                   :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                             "Accept"  "application/vnd.heroku+json; version=3"}}
                  {:name "agile-earth-5276"})

(def resp

(def  beans @resp)

(defn delete-all-heroku-apps
  "api-token as argument base64 encode"
  []
  (let [resp  (http/request {:method :get  :url "https://api.heroku.com/apps"
                              :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                                        "Accept"  "application/vnd.heroku+json; version=3"}}
                             (fn [{:keys [opts status body headers error] :as resp}]
                               (if error nil resp)))]
    (doseq [app (cheshire.core/parse-string (:body @resp) true)]
      @(http/request {:method :delete
                      :url (format  "https://api.heroku.com/apps/%s" (:name app))
                      :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                                "Accept"  "application/vnd.heroku+json; version=3"}}
                     {}))))

(delete-all-heroku-apps)






@(http/request  {:url "https://api.heroku.com/apps"
                 :method :post
                 :headers {"Authorization" "OjFmYjE4ZmJlLTg3MTMtNGE3Ny1hN2Q0LTc0YmU5ZjJjYjRiYwo="
                           "Accept"  "application/vnd.heroku+json; version=3"
                           }
}
                (fn [{:keys [opts status body headers error] :as resp}]
                  (if error  [1 resp] [ 0 resp])))



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
test123123987^%&!
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
