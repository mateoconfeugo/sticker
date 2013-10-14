(ns management.users
  (:require [cemerick.friend.credentials :refer [ hash-bcrypt]]
            [clj-stripe.common :refer [plan description trial-end coupon with-token execute]]
            [clj-stripe.customers :refer [email create-customer get-customer]]
            [clj-time.format :refer [show-formatters formatter unparse]]
            [clj-time.local :refer [local-now]]
            [clostache.parser :refer [render-resource]]
            [heroku-clj.core :refer [app-create set-api-key!]]
            [tentacles.repos :refer [create-repo]]
            [postal.core :refer [send-message]]
            [korma.core :refer [insert values]]
            [management.models.orm-spec :refer [user]]
            [flourish-common.db :refer [create-user-authoring-database]]))

(derive ::admin ::user)

(def users (atom {"friend" {:username "matthewburns@gmail.com"
                            :password (hash-bcrypt "clojure")
                            :pin "1234" ;; only used by multi-factor
                            :roles #{::user}}
                  "friend-admin" {:username "matthewburns@gmail.com"
                                  :password (hash-bcrypt "clojure")
                                  :pin "1234" ;; only used by multi-factor
                                  :roles #{::admin}}}))

(defn create-stripe-account
  [{:keys [user-id username user-email domain payment-gateway-token description-info]}]
  (with-token (str payment-gateway-token ":")
    (let [create-customer-op (create-customer   (email user-email) (description description-info)  (trial-end (+ 10000 (System/currentTimeMillis))))
          customer-result (execute create-customer-op)
          get-customer-op (get-customer (:id customer-result))
          customer-result (execute get-customer-op)]
      customer-result)))

(defn create-heroku-app
  [{:keys [account-id username description-info app-host-api-key] }]
  (do  (set-api-key! app-host-api-key)
       (app-create app-host-api-key (format "%s-%s" username account-id) "cedar")))

(defn create-git-repo
  [{:keys [user-id username domain description-info] }]
  (create-repo {:description description-info
                :homepage (format "%s-%s-%domain" user-id username domain)
                :public true :has-issues true :has-wiki true :has-downloads true}))

(defn prepare-promotional-offer
  "Create a custom promotion offer bundle for the user"
  []
  "<div><h1>This a test offer mention the number 42 and get a month free</h1></div>")

(defn send-welcome-email
  "sends the thank you message to person who just completed signup with a promotional offer"
  [{:keys [to from smtp-host subject username domain subject body]}]
  (let [offer-html (prepare-promotional-offer)
        ;;        body (render-resource (str app/root-dir "/" (:template-path  settings)) {:name name  :html-email offer-html})
        body "blah" ]
    (send-message {:from from :to [email] :subject subject :body body})))

(defn deploy-heroku-app
  "Pushes the git managed lein project for the user account combination into heroku and starts it running"
  [])

(defn new-user-signup
  "Sign up a user to the various resource services
  *: Add to mysql database so that relational type tasks are easy
  *: Document authoring  database and population
  *: Temporal database creation
  *: Create a lein user account based project
  *: Create a git project for the user account lein project
  *: Create a stripe account - applying the plan
  *: Create a heroku account - applying the plan
  *: Determine the new users offer bundle
  *: Email the new user with the bundle and relevant links
  *: Deploy the default user account to heroku
  *: Redirect user to the site builder editor viewing the default site in minimized editting mode with a jump to link or popup a new window
  *: Add monitoring "
  [{:keys [username password prefix] :as settings}]
  (let [created (unparse (formatter "yyyy-MM-dd hh:mm:ss") (local-now))
        input-data {:moniker  username :password password  :roles (pr-str  #{::user}) :created created :status "new"}
        user-id (:GENERATED_KEY (insert user (values input-data)))
;        git-url (create-git-repo (assoc input-data :user-id user-id))
                                        ;        doc-dbh (create-user-authoring-database prefix username)

;        stripe-account (create-stripe-account)
;        app (create-heroku-app )
;        email  (send-welcome-email input-data)
        ]
                                        ;   (deploy-heroku-app app)
    user-id))
