(ns landing-site.controllers.lead
  (:use [ring.util.response :only [content-type]]        
        [compojure.core :only [defroutes POST]]
        [landing-site.models.lead :only [log-lead]]))

(defroutes lead-gen-routes
  (POST "/lead" [lead_full_name lead_phone lead_email] 
    (log-lead {:full-name lead_full_name
               :phone-number lead_phone
               :email-address lead_email})))

