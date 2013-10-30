(ns management.controllers.admin
  ^{:author "Matthew Burns"
    :doc "Adminstratation of the application of itself or The aggregate business data of the total flourish online system"}
  (:require [compojure.core :refer [defroutes GET]]
            [management.views.admin :refer [admin-dashboard]]
            [ring.util.response :refer [content-type file-response]]))

(defroutes admin-mgmt-routes
  (GET "/admin/:admin-id" [id] (admin-dashboard {})))
