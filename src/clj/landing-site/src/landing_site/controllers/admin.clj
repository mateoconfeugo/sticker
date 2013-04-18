(ns landing-site.controllers.admin
  (:use [compojure.core :only (defroutes GET POST)])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [landing-site.views.admin :as view]
            [landing-site.models.admin :as model]))

(defn index []
  (view/index (model/all)))

(defn create [user]
  (when-not (str/blank? user)
    (model/create user))
  (ring/redirect "/"))

(defroutes routes
  (GET  "/" [] (index))
  (POST "/" [user] (create user)))