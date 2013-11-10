(ns management.controllers.api
  ^{:author "Matthew Burns"
    :doc "API controller for management functionality the client needs"}
  (:require [taoensso.timbre :refer [info]]
            [management.models.user :as user-model :refer [create]]
            [shoreleave.middleware.rpc :refer [remote-ns]]))

  (defn user-signup
    "This function takes the user signup information from the client and creates a new
   user account in the system"
    [{:keys [username email password confirmed-password] :as inputs}]
    (do (info (format "Creating user with username: %s" username))
        (user-model/create inputs)))

  (remote-ns 'management.controllers.api :as "api/mgmt")
