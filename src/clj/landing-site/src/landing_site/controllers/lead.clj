(ns landing-site.controllers.lead
  (:use [ring.util.response :only [content-type response redirect]]        
        [compojure.core :only [defroutes POST]]
        [landing-site.models.lead :only [log-lead]]
        [landing-site.config :only[cfg] :as app]
        [cheshire.core :only [parse-string generate-string]]
        [postal.core :only[send-message]]
        [clostache.parser :only[render-resource]]))

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
                       :email-address (:email post-data))
                     :full_name :phone :email)
        result (log-lead lead)]
    (if (:id result)
;;      (do
;;        (send-thank-you {:name (:full-name lead)  :email (:email-address lead)
;;                         :settings (-> app/cfg :lead :thank-you-email)})
        (-> (response (generate-string result)) (content-type "application/json"))
        ;;      (-> (redirect (str (-> app/cfg :lead :redirect-uri) "/"  (:full-name lead))))
      ;;        (-> (redirect (str (-> app/cfg :lead :redirect-uri))))
      {:status 500 :headers {} :body (generate-string result)})))

      
(defroutes lead-gen-routes
  (POST "/lead" {body :body} (handle-lead (parse-string (slurp body) true))))

