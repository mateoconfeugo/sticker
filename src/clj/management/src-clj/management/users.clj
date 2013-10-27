(ns management.users
  (:require [cemerick.friend.credentials :refer [ hash-bcrypt]]
            [clj-stripe.common :refer [plan description trial-end coupon with-token execute card]]
            [clj-stripe.customers :refer [email create-customer get-customer]]
            [clj-time.format :refer [show-formatters formatter unparse]]
            [clj-time.local :refer [local-now]]
            [clj-time "0.6.0"]
            [clostache.parser :refer [render-resource]]
            [clj-heroku-api.core :as heroku :refer [create-app remove-app app-exists? list-apps add-config list-config remove-config
                                                    add-key remove-key obtain-key list-keys]]
;;            [heroku-clj.core :refer [app-create set-api-key!]]
            [tentacles.repos :as github :refer [create-repo create-hook create-key]]
            [postal.core :refer [send-message]]
            [korma.core :refer [insert values]]
            [management.models.orm-spec :refer [user]]
            [flourish-common.db :refer [create-user-authoring-database]]))

(derive ::admin ::user)

;;  TODO: Remove this perhaps add
(def users (atom {"friend" {:username "matthewburns@gmail.com"
                            :password (hash-bcrypt "clojure")
                            :pin "1234" ;; only used by multi-factor
                            :roles #{::user}}
                  "friend-admin" {:username "matthewburns@gmail.com"
                                  :password (hash-bcrypt "clojure")
                                  :pin "1234" ;; only used by multi-factor
                                  :roles #{::admin}}}))

(defn create-stripe-account
  [{:keys [user-id username user-email domain payment-gateway-token description-info card]}]
  (with-token (str payment-gateway-token ":")
    (execute (create-customer card (email user-email) (description description-info)))))

(defn prepare-promotional-offer
  "Create a custom promotion offer bundle for the user"
  []
  "<div><h1>This a test offer mention the number 42 and get a month free</h1></div>")

(comment
  (defn add-deployment-hooks
    "Adds the functionality of triggering heroku to check out and deploy
   the version containing the newly checked in changes"
    [{:keys [git-repository-name git-username git-password  git-api-token git-auth] :as settings}]
    (add-webhook (assoc settings :git-events ["push" "pull_request"]))))

(defn add-webhook
  "Adds a git webhook that posts to the giving url when the subscripted to events occur"
  [{:keys [git-hook-name git-repository-name git-username git-password  git-api-token git-auth target-uri git-events] :as settings}]
  (git-repo/create-hook git-username git-repository-name "web" {:url target-uri :content_type "json"} {:events git-events  :active true :auth git-auth}))


(comment
  Creating new heroku user application
  * login in using the the common authentication ssh key shared between heroku and github
  * create a heroku app
  * add heroku hook to github
Tools
* Heroku
* Github
* Linux
* Clojure
* Leinginen
* Pallet
  )

(comment
  * Clone the new github project into a staging directory
  * Move skeleton deployment project into staging directy
  * Create the heroku app via the command line
  * Configure heroko enivornment variables
  * Setup the heroku add ons
  * Add the resource edn files
  * Check in the skeleton project with changes and the webhook should cause the application to be launched)

(defn provision-app-to-deployment-server
  "Add the hosted application to the deployment server so it can deploy the lastest version upon  webhook triggered request"
  [{:keys [deployment-app-name new-app-name deployment-app-account-api-token git-auth
           private-key git-repo-root] :as args}]
  (let [app-ssh {(format "%s_SSH_KEY" new-app-name) private-key}
        app-git-repo {(format "%s_GITHUB_REPO" new-app-name) (format "git@github.com/%s/%s.git" git-repo-root new-app-name)}
        heroku-repo {(format "%s_HEROKU_REPO" new-app-name) "git@heroku.com"}
        cfgs (merge app-ssh app-git-repo heroku-repo)]
    (heroku/add-config deployment-app-name cfg deployment-app-account-api-token)))

(defn create-app-lein-project
  "Creates the data driven lein project.  Any data that an be is stored in edn format in the resources diretory.
   This done by using a lein template and then serializing the resource model data into a file in the resource dir.
   All of the files need to create a application's project are first created in a staging area then checked into
   github "
  [{:keys [resource-model] :as args}])

(defn initial-app-check-in
  "Push the new customized lein project to the new github project triggering the newly create webhook to generate
  a request to the deployment server that will checkout the lein project into the appplication hosting environment
  buildit and start running the application"
  [{:keys [git-repo git-auth staging-dir-path] :as args }])

(defn deploy-app
  "Connects  the git managed soon to be created lein project for the user account combination into heroku and starts it running"
  [{:keys [username repo-name app-name api-token git-auth] :as args}]
  (let [app (heroku/create-app api-token)
        app-name (get-name new-app)
        agent (ssh-agent {:use-system-ssh-agent false})
        [public-key private-key] (generate-key agent :rsa 2048 "")
        key-title (format "deployment-key-%s" app-name)
        git-repo-add-key-result (github/create-key username repo-name key-title public-key {:auth git-auth})
        heroku-app-add-key-result (heroku/add-key public-key api-token)]
    {:app app
     :deployment-key-name key-title
     :public-key public-key
     :private-key private-key}))

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
  [{:keys [user-email username password prefix git-username git-password  git-api-token payment-gateway-token
           smtp-host email-saas-api-token  welcome-email-address welcome-subject description domain
           hosted-app-name hosting-api-token deployment-server-name git-account-name] :as settings}]
  (let [created (unparse (formatter "yyyy-MM-dd hh:mm:ss") (local-now))
        input-data {:moniker  username :password password  :roles (pr-str  #{::user}) :created created :status "new"}
        user-id (:GENERATED_KEY (insert user (values input-data)))
        account-id 1
        stripe-account (create-stripe-account  settings)
        git-auth (format "%s:%s" git-username git-password)
        repo-name (format "%s-%s-%s"  domain username account-id)
        git-repo (github/create-repo repo-name {:auth git-auth :description description :public true :has-issues true :has-wiki true :has-downloads true})
        deployement-data (deploy-app username repo-name app-name api-token git-auth)
        provisioned-app-cfgs (provision-app-to-deployment-server (merge args {:deployment-app-name deployment-server-name
                                                                              :new-app-name new-app-name
                                                                              :deployment-app-account-api-token api-token
                                                                              :private-key private-key
                                                                              :git-repo-root git-account-name}))
        project-dir (create-app-lein-project repo-name resource-model)
        deployment-result (initial-app-check-in  project-dir git-repo deployed-app git-auth)
;;        repo (create-repo repo-name {:oauth git-api-token :description description :public true :has-issues true :has-wiki true :has-downloads true})
        offer-html (prepare-promotional-offer)
        sending-result (send-message ^{:host  smtp-host
                                       :user email-saas-api-token
                                       :pass email-saas-api-token}
                                     {:from welcome-email-address
                                      :to user-email
                                      :subject welcome-subject
                                      :body offer-html})
        ;; doc-dbh (create-user-authoring-database prefix username)
        ;; app (create-heroku-app )
        ;; deployment-result (deploy-heroku-app app)
        ;; _ (info deployment-result)
        ]
    ;; deployment-result
    user-id))
