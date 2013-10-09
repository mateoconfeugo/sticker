(ns ^{:name "Sign-up and redirect"
      :doc "Form-based all-in-one sign-up and redirect to authenticated space."}
  management.signup-and-redirect
  (:require [cemerick.friend :as friend]
            ;;   [cemerick.friend :refer [authorize logout authenticate wrap-authorize]]
            [cemerick.friend.credentials  :as creds :refer [bcrypt-credential-fn]]
            [cemerick.friend.workflows  :as workflows :refer [interactive-form]]
            [clj-time.format :refer [show-formatters formatter unparse]]
            [clj-time.local :refer [local-now]]
            [compojure.core :as compojure :refer (GET POST ANY defroutes context routes)]
            [compojure.handler :as handler :refer [site]]
            [compojure.route :as route :refer [files resources not-found]]
            [ring.util.response :as resp]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [clojure.string :as str]
            [clojure.edn :as edn :refer [read-string]]
            [hiccup.page :as h]
            [hiccup.element :as e]
            [korma.core :refer [insert values select where fields]]
            [taoensso.timbre :refer [info]]
            [management.misc :as misc]
            [management.models.user :as user-model :refer [create]]
            [management.models.orm-spec :as orm :refer [user]]
            [management.users :as users :refer (users)]
            [site-builder-udc.routes :as site-builder-editor :refer [all-routes]]
            [site-builder-udc.views.editor :refer [editor]]
            [management.views.tools :refer [user-dashboard]]
            [management.views.admin :refer [admin-dashboard]]
            [management.models.tools :refer [dashboard-config]]))

(defn wrap-add-anti-forgery-cookie [handler & [opts]]
  "Mimics code in Shoreleave-baseline's customized ring-anti-forgery middleware."
  (fn [request]
    (let [response (handler request)]
      (if-let [token (-> request :session (get "__anti-forgery-token"))]
        (assoc-in response [:cookies "__anti-forgery-token"] token)
        response))))


(defn- create-user
  "Probably useless function that needs to be removed"
  [{:keys [username password admin] :as user-data}]
  (-> (dissoc user-data :admin)
      (assoc :identity username
             :password (creds/hash-bcrypt password)
             :roles (into #{::users/user} (when admin [::users/admin])))))

(defn lookup-user
  "Retrieve the authenticaten map for a username against the database"
  [username]
  (let [model (first (select user (fields [:moniker :username ] :password :roles) (where {:moniker username})))
        tmp (assoc-in model [:password] (creds/hash-bcrypt (:password model)))
        tmp1 (assoc-in tmp [:roles] (edn/read-string (:roles model)))]
    tmp1))

(defn- signup-form
  [flash]
  [:div {:class "row"}
   [:div {:class "columns small-12"}
    [:h3 "Sign up "
     [:small "(Any user/pass combination will do, as you are creating a new account or profile.)"]]
    [:div {:class "row"}
     [:form {:method "POST" :action "signup" :class "columns small-4"}
      [:div "Username" [:input {:type "text" :name "username" :required "required"}]]
      [:div "Password" [:input {:type "password" :name "password" :required "required"}]]
      [:div "Confirm" [:input {:type "password" :name "confirm" :required "required"}]]
      [:div "Make you an admin? " [:input {:type "checkbox" :name "admin"}]]
      [:div
       [:input {:type "submit" :class "button" :value "Sign up"}]
       [:span {:style "padding:0 0 0 10px;color:red;"} flash]]]]]])

(def login-form
  [:div {:class "row"}
   [:div {:class "columns small-12"}
    [:h3 "Login"]
    [:div {:class "row"}
     [:form {:method "POST" :action "login" :class "columns small-4"}
      [:div "Username" [:input {:type "text" :name "username"}]]
      [:div "Password" [:input {:type "password" :name "password"}]]
      [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]])

(compojure/defroutes site-routes
  (GET "/" req (resp/file-response "templates/signup.html" {:root "resources"}))
  (GET "/login" req (h/html5 misc/pretty-head (misc/pretty-body login-form)))
  (GET "/logout" req (friend/logout* (resp/redirect (str (:context req) "/"))))
  (GET"/mgmt/user/:id" [id] (friend/authorize  #{::users/user} (user-dashboard {:display-map (dashboard-config {:user-id id})})))
  (GET "/mgmt/site-builder/editor" [] (friend/authorize   #{::users/user} (editor {})))
  (GET "/admin/:admin-id" [id] (friend/authorize #{::users/admin} (admin-dashboard {})))
  (POST "/signup" {{:keys [username password confirm] :as params} :params :as req}
        (if (and (not-any? str/blank? [username password confirm]) (= password confirm))
          (let [user-data (create-user (select-keys params [:username :password :admin]))
                custom-formatter (formatter "yyyy-MM-dd hh:mm:ss")
                created (unparse custom-formatter (local-now))
                input-data {:moniker  username :password password  :roles (pr-str #{::users/user}) :created created :status "new"}
                user-id (:GENERATED_KEY (insert user (values input-data)))]
            (friend/merge-authentication
             (resp/redirect (format "mgmt/user/%s" user-id))  user-data))
          (assoc (resp/redirect (str (:context req) "/")) :flash "passwords don't match!")))
  (GET "/dashboard" req
       (friend/authenticated
        (let [username  (-> req :session :cemerick.friend/identity :current)
              user-data (first (select user (where {:moniker username})))
              _ (info username)
             user-dashboard-uri (format "/mgmt/user/%s" (:id user-data))
             _ (info user-dashboard-uri)]
          (resp/redirect user-dashboard-uri))))
  (GET "/old-signup" req
       (h/html5
        misc/pretty-head
        (misc/pretty-body
         (misc/github-link req)
         [:h2 "Sign up Now!"]
         [:p "Flourish Online Marketing With Gusto"]
         [:h3 "Current Status " [:small "(this will change when you log in/out)"]]
         [:p (if-let [identity (friend/identity req)]
               (apply str "Logged in, with these roles: "
                      (-> identity friend/current-authentication :roles pr-str))
               "anonymous user")]
         (signup-form (:flash req))
         [:p (e/link-to (misc/context-uri req "logout") "Click here to log out") "."]))))
(comment
  (GET "/dashboard"  req
       (friend/authenticated
        (let [username (:user (req :params))
             user-data (first (select user (where {:moniker username})))
             user-dashboard-uri (format "/mgmt/user/%s" (:id user-data))
             _ (info user-dashboard-uri)]
          (resp/redirect user-dashboard-uri))))
  )

(def static-routes (compojure/routes
                    (route/resources "/")
                    (route/resources "/design/" {:root "templates"})
                    (route/files "/" {:root "public"})
                    (route/not-found "Not Found")))


(def authenticated-routes (friend/authenticate
                           site-routes
                           {:allow-anon? true
                            :login-uri "/login"
                            :default-landing-uri "/dashboard"
                            :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                                       resp/response
                                                       (resp/status 401))
                            :credential-fn (partial creds/bcrypt-credential-fn lookup-user)
                            :workflows [(workflows/interactive-form)]}))

;;(def page (wrap-add-anti-forgery-cookie (wrap-anti-forgery (handler/site (compojure/routes authenticated-routes static-routes)))))
(def page (handler/site (compojure/routes authenticated-routes static-routes)))
