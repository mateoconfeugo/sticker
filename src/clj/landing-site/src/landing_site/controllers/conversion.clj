(ns landing-site.controllers.conversion
  "Web application functionality handling conversion and post conversion activity"
  (:use [compojure.core :only [defroutes GET]]
        [landing-site.views.thank-you :only[render] :as thank-you]))

(defroutes conversion-routes (GET "/thank-you/:name" [name] (thank-you/render name)))