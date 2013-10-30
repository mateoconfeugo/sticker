(ns management.routes
  ^{:author "Matthew Burns"
    :doc "Assemble the routes clients use to access the static and dynamically generated resources
          that make up the application. Compose the rpc api from the business components"}
  (:refer-clojure :exclude [escape-html]) ; suppress the shadowing warning
  (:require ;; Authentication and authorization framework
   [cemerick.friend :refer [authorize logout authenticate wrap-authorize]]
   [cemerick.friend.credentials  :refer [bcrypt-credential-fn]]
   [cemerick.friend.workflows :refer [interactive-form]]
   ;; Web Application Framework
   [compojure.core :refer [routes defroutes context]]
   [compojure.handler :as handler :refer [site]]
   [compojure.route :refer[files resources not-found]]
   ;; Application WDC Business Components
   [site-builder-udc.routes :as site-builder-editor :refer [all-routes]]
   [management.controllers.public :refer [public-routes]]
   [management.controllers.tools :refer [user-mgmt-routes]]
   [management.controllers.admin :refer [admin-mgmt-routes]]
   ;; Client API
   [management.controllers.api]
   ;; Prevent cross domainn access of api resources
   [ring.middleware.file :refer [wrap-file] ]
   [ring.middleware.file-info :refer [wrap-file-info] ]
   [ring.middleware.stacktrace :refer [wrap-stacktrace]]
   [ring.middleware.reload :refer [wrap-reload]]
   [ring.middleware.logger :only [wrap-with-logger]]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.middleware.logger :refer [wrap-with-logger]]
   [ring.util.response :refer [response status]]
   [management.users :refer [users]]
   ))

;; TODO: Install channel security so that the stripe payment gateway related functionality is made to use https

;; Permission hierarchy
;;(derive ::admin ::user) ; give all the privileges available to user to the admin
;;(derive ::publisher ::user) ; give all the privileges available to user to the publisher
;;(derive ::advertiser ::user) ; give all the privileges available to user to the advertiser
;;(derive ::admin ::publisher) ; give all the privileges available to publisher to the admin
;;(derive ::admin ::advertiser) ; give all the privileges available to advertiser to the admin


(defn wrap-add-anti-forgery-cookie [handler & [opts]]
  "Mimics code in Shoreleave-baseline's customized ring-anti-forgery middleware."
  (fn [request]
    (let [response (handler request)]
      (if-let [token (-> request :session (get "__anti-forgery-token"))]
        (assoc-in response [:cookies "__anti-forgery-token"] token)
        response))))

(def user-routes (routes
;;                  site-builder-editor/all-routes
                  user-mgmt-routes))


(def admin-routes (routes
;;                   site-builder-editor/all-routes
                   admin-mgmt-routes))
(defroutes authorized-user-routes (context "/" request (wrap-authorize user-routes #{::user})))
(defroutes authorized-admin-routes (context "/" request (wrap-authorize admin-routes #{::admin})))

;;(defroutes authorized-user-routes (wrap-authorize user-routes #{::user}))
;;(defroutes authorized-admin-routes (wrap-authorize admin-routes #{::admin}))

(def authorized-routes (routes authorized-user-routes authorized-admin-routes))

(defroutes static-routes
  (resources "/")
  (resources "/design/" {:root "templates"})
  (files "/" {:root "public"})
  (not-found "Not Found"))

;;(def webapp (site (routes public-resources-routes authorized-routes)))
(def webapp (site (->   (routes public-routes
                                static-routes
                                (authenticate authorized-routes
                                              {:allow-anon? true?
                                               :login-uri "/login"
                                               :default-landing-uri "/mgmt/user/1"
                                               :credential-fn #(bcrypt-credential-fn @users %)
                                               :workflows [(interactive-form)]
                                               :unauthorized-handler #(-> "<html><body><div><h2>Not authorized</h2></div></body></html>"
                                                                          response
                                                                          (status 401))}))
                        wrap-with-logger
                        wrap-file-info
                        (wrap-reload '(management))
                        wrap-stacktrace)))


(comment
  wrap-with-logger
  wrap-add-anti-forgery-cookie
  wrap-anti-forgery)
