(ns management.users
  (:require [cemerick.friend.credentials :refer [hash-bcrypt]]
            [clj-stripe.common :refer [plan description trial-end coupon with-token execute card]]
            [clj-stripe.customers :refer [email create-customer get-customer]]
            [clj-time.format :refer [show-formatters formatter unparse]]
            [clj-time.local :refer [local-now]]
            [clj-ssh.ssh :refer [ssh-agent generate-keypair]]
            [clostache.parser :refer [render-resource]]
            [clojure.edn]
;;            [leiningen.new.landing-site :refer [create-landing-site-project]]
;;            [leiningen.new.templates :refer [renderer name-to-path ->files]]
;;            [heroku-clj.core :as heroku :refer [add-config]]
            [clj-jgit.porcelain :refer [git-clone-full git-add git-commit]]
            [dmgr.core :refer [run expand-path git-push-heroku-master]]
            [tentacles.repos :as github :refer [create-repo create-hook create-key]]
            [postal.core :refer [send-message]]
            [korma.core :refer [insert values]]
            [management.models.orm-spec :refer [user]]
            [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [flourish-common.db :refer [create-user-authoring-database]])
  (:import [java.io FileNotFoundException File]
         [com.heroku.api HerokuAPI]
         [com.heroku.api App ]
         [com.heroku.api.request.app AppDestroy ]
         [com.heroku.api Heroku$Stack]
         [com.heroku.api.connection Connection]
         [com.heroku.api.connection ConnectionFactory]))


(def render (renderer "landing-site"))

(defn landing-site
  "Creates the custom landing site web server for a user-account"
  [name resource-model-uri]
  (let [resource-model (clojure.edn/read-string (slurp resource-model-uri))
        data (assoc resource-model :name name :sanitized (name-to-path name))]
    (->files data
             ["resources/templates/head.html" (render "head.html" data)]
             ["resources/templates/navigation.html" (render "navigation.html" data)]
             ["resources/templates/carousel_banner.html" (render "carousel_banner.html" data)]
             ["resources/templates/dropdown_lead_modal.html" (render "dropdown_lead_modal.html" data)]
             ["resources/templates/free_referral_offer.html" (render "free_referral_offer.html" data)]
             ["resources/templates/header.html" (render "header.html" data)]
             ["resources/templates/inline_lead_form.html" (render "inline_lead_form.html" data)]
             ["resources/templates/scripts.html" (render "scripts.html" data)]
             ["resources/templates/index.html" (render "index.html" data)]
             ["resources/templates/top_banner.html" (render "top_banner.html" data)]
             ["resources/templates/thc_logo.html" (render "thc_logo.html" data)]
             ["resources/templates/market_message.html" (render "market_message.html" data)]
             ["resources/templates/footer.html" (render "footer.html" data)]
             ["resources/templates/lead_modal.html" (render "lead_modal.html" data)]
             ["resources/templates/carousel.html" (render "carousel.html" data)]
             ["resources/templates/release.html" (render "release.html" data)]
             ["resources/templates/dissolve-away.html" (render "dissolve-away.html" data)]
             ["resources/templates/thank_you.html" (render "thank_you.html" data)]
             ["README.md" (render "README.md" data)]
             ["project.clj" (render "project.clj" data)]
             ["resources/market_matrix.edn" (render "market_matrix.edn" resource-model)]
             ["resources/config.edn" (render "config.edn" resource-model)]
             ["src-cljs/{{sanitized}}/main.cljs" (render "main.cljs" data)]
             ["src-cljs/{{sanitized}}/layout_manager.cljs" (render "layout_manager.cljs" data)]
             ["src-cljs/{{sanitized}}/repl.cljs" (render "repl.cljs" data)]
             ["src-clj/{{sanitized}}/handler.clj" (render "handler.clj" data)]
             ["src-clj/{{sanitized}}/server.clj" (render "server.clj" data)]
             ["src-clj/{{sanitized}}/routes.clj" (render "routes.clj" data)]
             ["src-clj/{{sanitized}}/config.clj" (render "config.clj" data)]
             ["src-clj/{{sanitized}}/controllers/api.clj" (render "api.clj" data)]
             ["src-clj/{{sanitized}}/controllers/site.clj" (render "site.clj" data)]
             ["src-clj/{{sanitized}}/models/landing_sites.clj" (render "landing_sites.clj" data)]
             ["src-clj/{{sanitized}}/models/market_matrices.clj" (render "market_matrices.clj" data)]
             ["src-clj/{{sanitized}}/models/market_vectors.clj" (render "market_vectors.clj" data)]
             ["src-clj/{{sanitized}}/views/host_dom.clj" (render "host_dom.clj" data)])))


(defn create-landing-site-project
  "Create a data driven landing site project from leiningen template using the provided
   model at the desired location.  Any data that an be is stored in edn format in the resources diretory"
  [{:keys [ project-name model-path destination-path] :as args}]
;  (binding [leiningen.new.templates/*dir* (File. destination-path)]
;    (landing-site project-name model-path destination-path)
  )


(defn remove-heroku-app
  [name api-token]
  (let [conn (ConnectionFactory/get)]
    (.execute conn (AppDestroy. name) api-token)))

(defn new-heroku-app
  "Create a new heroku application returns"
  [api]
  (let [conn (ConnectionFactory/get)
        app (-> (App. )
                (.on com.heroku.api.Heroku$Stack/Cedar))]
    (.createApp api app)))

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

(defn provision-app-to-deployment-server
  "Add the hosted application to the deployment server so it can deploy the lastest version upon  webhook triggered request"
  [{:keys [deployment-app-name new-app-name deployment-app-account-api-token git-auth
           private-key git-repo-root] :as args}]
  (let [app-ssh {(format "%s_SSH_KEY" new-app-name) private-key}
        app-git-repo {(format "%s_GITHUB_REPO" new-app-name) (format "git@github.com/%s/%s.git" git-repo-root new-app-name)}
        heroku-repo {(format "%s_HEROKU_REPO" new-app-name) "git@heroku.com"}
        cfgs (merge app-ssh app-git-repo heroku-repo)]
    nil
;    (heroku/add-config deployment-app-name cfg deployment-app-account-api-token)
    ))

(defn create-stripe-account
  [{:keys [user-id username user-email domain payment-gateway-token description-info card]}]
  (with-token (str payment-gateway-token ":")
    (execute (create-customer card (email user-email) (description description-info)))))

(defn prepare-promotional-offer
  "Create a custom promotion offer bundle for the user"
  []
  "<div><h1>This a test offer mention the number 42 and get a month free</h1></div>")

(defn install-heroku-app
  "Connects  the git managed soon to be created lein project for the user account combination into heroku and starts it running"
  [{:keys [username repo-name app-name api-token git-auth] :as args}]
  (let [app (new-heroku-app api-token)
        app-name (.getName app)
        agent (ssh-agent {:use-system-ssh-agent false})
        [public-key private-key] (generate-keypair agent :rsa 2048 "")
        key-title (format "deployment-key-%s" app-name)
;;        git-repo-add-key-result (github/create-key username repo-name key-title public-key {:auth git-auth})
;;        heroku-app-add-key-result (heroku/add-key public-key api-token)
        ]
    {:app app
     :app-name app-name
     :deployment-key-name key-title
     :public-key public-key
     :private-key private-key}))

(defn add-webhook
  "Adds a git webhook that posts to the giving url when the subscripted to events occur"
  [{:keys [repo-name git-username git-auth target-uri ] :as settings}]
  (github/create-hook git-username repo-name "web" {:url target-uri :content_type "json"}
                        {:events ["push" "pull_request"] :active true :auth git-auth}))

(defn create-custom-scm-app
  "Push the new customized lein project to the new github project triggering the newly create webhook to generate
  a request to the deployment server that will checkout the lein project into the appplication hosting environment
  buildit and start running the application"
  [{:keys [git-username git-password domain  username account-id description
           staging-dir-path hosted-app-name] :as args }]
  (let [git-auth (format "%s:%s" git-username git-password)
        repo-name (format "%s-%s-%s"  domain username account-id)
        github-repo-data (github/create-repo repo-name {:auth git-auth :description description :public true :has-issues true :has-wiki true :has-downloads true})
        staging-dir (expand-path (format "%s/%s" staging-dir-path repo-name))
        cloned-git-repo (git-clone-full (:url github-repo-data) staging-dir)
        ;;        resource-model-uri (assemble-landing-site-model )
       resource-model-uri {}
        lein-project-dir (create-landing-site-project hosted-app-name resource-model-uri)
        _ (git-add cloned-git-repo lein-project-dir)
        _ (git-commit cloned-git-repo "Initial adding of lein template generated files to the github project repo")
        shell-push-cmd (format "cd %s  && git push origin" staging-dir)
        _ (run staging-dir shell-push-cmd)]
    cloned-git-repo))

(defn new-user-signup
  "Sign up a user to the various resource services
  *:x Add to mysql database so that relational type tasks are easy
  *: Document authoring  database and population
  *: Temporal database creation
  *:x Create a lein user account based project
  *:x Create a git project for the user account lein project
  *:x Create a stripe account - applying the plan
  *:x  Create a heroku account - applying the plan
  *:x Determine the new users offer bundle
  *:x Email the new user with the bundle and relevant links
  *:x Deploy the default user account to heroku
  *: Redirect user to the site builder editor viewing the default site in minimized editting mode with a jump to link or popup a new window
  *: Add monitoring "

  [{:keys [user-email username password prefix git-username git-password  git-api-token payment-gateway-token
           smtp-host email-saas-api-token  welcome-email-address welcome-subject description domain
           hosted-app-name hosting-api-token deployment-server-name git-account-name repo-staging-dir] :as settings}])

(comment

  (let [created (unparse (formatter "yyyy-MM-dd hh:mm:ss") (local-now))
        input-data {:moniker  username :password password  :roles (pr-str  #{::user}) :created created :status "new"}
        user-id (:GENERATED_KEY (insert user (values input-data)))
        account-id 1
        stripe-account (create-stripe-account  settings)
        app-git-repo (create-custom-scm-app (assoc settings :account-id account-id))
        heroku-app (create-heroku-app {:username username :repo-name nil :api-token nil git-auth nill })
        app-name (:app-name heroku-app)
        trigger-uri (format "http://%s/deploy?app=%s&key=%s" deployment-server app-name deployment-key)
        _ (add-webhook {:repo-name repo-name :git-username git-username :git-auth git-auth :target-uri trigger-uri})
        (git-push-heroku-master {:app-name app-name :staging-parent-dir-path repo-staging-dir
                                 :src-repo-uri (format "https://github.com/%s/%s.git" git-username app-name)
                                 :src-repo-remote "origin" :branch-name "master"})
;;        deployement-data (deploy-app username repo-name app-name api-token git-auth)
        provisioned-app-cfgs (provision-app-to-deployment-server (merge args {:deployment-app-name deployment-server-name
                                                                              :new-app-name new-app-name
                                                                              :deployment-app-account-api-token api-token
                                                                              :private-key private-key
                                                                              :git-repo-root git-account-name}))

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
    user-id)
  )
