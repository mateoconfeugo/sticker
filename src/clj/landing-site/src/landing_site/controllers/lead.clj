(ns landing-site.controllers.lead
  "Store leads, send out thank you email and redirect to correct page"
  (:use [cheshire.core :only [parse-string generate-string]]
        [clostache.parser :only[render-resource]]        
        [compojure.core :only [defroutes POST]]
        [landing-site.config :only[cfg] :as app]
        [landing-site.models.lead :only [log-lead]]
        [postal.core :only[send-message]]
        [ring.util.response :only [content-type response redirect]]))
        
(defn send-thank-you
  [{:keys[name email settings]}]
  "sends the thank you message to person who just completed the lead"  
  (let [from (:from settings)
        subject (:subject settings)
        body (render-resource (:template-path  settings) {:name name})]
    (send-message {:from from :to [email] :subject subject :body body})))

(defn handle-lead
  [post-data]
  "format lead data, store it, send you email, redirect to conversion page"
  (let [lead (dissoc (assoc post-data
                       :full-name (:full_name post-data)
                       :phone-number (:phone post-data)
                       :email-address (:email post-data)
                       :full_name :phone :email))
        lead-name (:full-name lead)
        lead-email (:email-address lead)
        email-settings (-> app/cfg :lead :thank-you-email)
        result (log-lead lead)]
    (if-let [id (:id result)]
      (do
        (send-thank-you {:name lead-name  :email lead-email :settings email-settings})
        (-> (redirect (str (-> app/cfg :lead :redirect-uri) "/"  (:full-name lead)))))
      {:status 500 :headers {} :body (generate-string result)})
    result))
      
(defroutes lead-gen-routes
  (POST "/lead" {body :body} (handle-lead (parse-string (slurp body) true))))


;;(-> (response (generate-string result)) (content-type "application/json"))        
;;        (-> (redirect (str (-> app/cfg :lead :redirect-uri))))
