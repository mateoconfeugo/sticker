(ns management.controllers.tools
  ^{:author "Matthew Burns"
    :doc   "The hosting dom for the users customized version of the managment tool they use
            to interact and use the various features of the flourish online system"}
  (:require [compojure.core :refer [defroutes GET]]
        [management.views.tools :refer [user-dashboard]]
        [management.models.tools :refer [dashboard-config]]))

(defroutes user-mgmt-routes
  (GET"/mgmt/user/:id" [id] (user-dashboard {:display-map (dashboard-config {:user-id id})})))
