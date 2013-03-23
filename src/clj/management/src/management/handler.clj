(ns management.handler
  (:require [compojure.handler :as handler]
            [cheshire.core :as json]
            [ring.util.response :as resp]
            [compojure.route :as route])
  (:require [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]))
  (:use [hiccup.page :only (html5)]
        [ring.adapter.jetty :as ring]
        [compojure.core]
        [management.controllers.user :only(user-routes) :as gui-user]
        [management.views.layout :as layout]))

(def users {"root" {:username "root"
                    :password (creds/hash-bcrypt "sa")
                    :roles #{::admin}}
            "paul" {:username "paul"
                    :password (creds/hash-bcrypt "sa")
                    :roles #{::publisher}}
            "ann" {:username "ann"
                    :password (creds/hash-bcrypt "sa")
                   :roles #{::advertiser}}
            "carl" {:username "carl"
                    :password (creds/hash-bcrypt "sa")
                    :roles #{::content-provider}}
            "freddy" {:username "freddy"
                    :password (creds/hash-bcrypt "sa")
                    :roles #{::feed-provider}}
            "jane" {:username "jane"
                    :password (creds/hash-bcrypt "sa")
                    :roles #{::user}}})

(derive ::admin ::user)
(derive ::publisher ::user)
(derive ::advertiser ::user)
(derive ::content-provider ::user)
(derive ::feed-provider ::user)


(defn run-of-network [] (str "run of network yall"))

(defn index []
  (html5
   [:head
    [:title "Welcome to Flourish Online Management"]]
   [:body
    [:div {:id "content"} "Flourish Online Management"]
    [:div {:id "content"}
     [:h1 "Interesting"]
     ]    
    ]))

(comment
(defroutes app-routes
  gui-user/user-routes
  (
   (route/resources "/")
   (route/not-found (run-of-network))))

(def app (handler/site app-routes))

(def secured-app
  (-> app
      (friend/authenticate {:credential-fn (partial creds/bcrypt-credential-fn users)
                            :workflows [(workflows/interactive-form)] })))
)

(defn- json-response
  [x]
  (-> (json/generate-string x)
      resp/response
          (resp/content-type "application/json")))

(defroutes app-routes
  (GET "/" [] "welcome")
  (GET "/login" [] (ring.util.response/file-response "login.html" {:root "resources"}))
  (GET "/clientconfig" [] (resp/content-type (ring.util.response/file-response "clientconfig.json" {:root "resources"})  "application/json"))  
  (GET "/admin" request
       (friend/authorize #{::admin} (ring.util.response/file-response "index.html" {:root "resources"})))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))
  (GET "/authorized" request
       (friend/authorize #{::user} "This page can only be seen by authenticated users."))
  (route/resources "/")
  (route/files "/" {:root "public"})
  (route/not-found "Not Found"))

(def app
  (handler/site
   (friend/authenticate app-routes
                        {:credential-fn (partial creds/bcrypt-credential-fn users)
                                                  :workflows [(workflows/interactive-form)]})))


(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8087"))]
    (start port)))
