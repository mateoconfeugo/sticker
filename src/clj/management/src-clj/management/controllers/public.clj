(ns management.controllers.public
  ^{:author "Matthew Burns"
    :doc "Entrance and Exit webpages of the application along with a redirect to the login page"}
  (:require [cemerick.friend :refer [logout*]]
            [compojure.core :refer [defroutes GET]]
            [ring.util.response :refer [file-response redirect]]))

(defroutes public-routes
  (GET "/" [] (redirect "/login"))
  (GET "/logout" req (logout* (redirect (str (:context req) "/"))))
  (GET "/signup" [req] (file-response "templates/signup.html" {:root "resources"}))
  (GET "/login" [] (file-response "templates/login.html" {:root "resources"})))
