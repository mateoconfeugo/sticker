(ns lead-generation.controllers.lead
  "Store leads, send out thank you email and redirect to correct page"
  (:require [cheshire.core :refer [parse-string generate-string]]
        [clostache.parser :refer[render-resource]]        
        [compojure.core :refer [defroutes POST]]
        [flourish-common.web-page-utils :refer [run-server render-to-response render-request]]
        [lead-generation.config :refer[cfg root-dir] :as app]
        [lead-generation.models.lead :refer [log-lead log-support-lead]]
        [postal.core :refer[send-message]]
        [ring.util.response :refer [content-type response redirect]]))
        
(defn send-thank-you
  [{:keys[name email settings]}]
  "sends the thank you message to person who just completed the lead"  
  (let [from (:from settings)
        subject (:subject settings)
        body (render-resource (str app/root-dir "/" (:template-path  settings)) {:name name})]
    (send-message {:from from :to [email] :subject subject :body body})))

(defmulti handle-lead
  (fn [post-data] (contains?  post-data :postal-code))
  :default false)

(defmethod handle-lead true
  [post-data]
  (let [lead (dissoc (assoc post-data
                       :full-name (:full_name post-data)
                       :phone-number (:phone post-data)
                       :email-address (:email post-data)
                       :postal-code (:postal_code post-data))
                       :full_name :phone :email :postal_code)
        lead-name (:full-name lead)
        lead-email (:email-address lead)
        email-settings (-> app/cfg :lead :thank-you-email)
        result (log-support-lead lead)]
    (if (:id result)
      (-> (response (generate-string {:redirect-url (str (-> app/cfg :lead :redirect-uri) "/"  (:full-name lead))}))
                (content-type "application/json"))
      {:status 500 :headers {} :body (generate-string result)})))



;;(defn handle-lead
(defmethod handle-lead false
  [post-data]
  "format lead data, store it, send you email, redirect to conversion page"
  (let [lead (dissoc (assoc post-data
                       :full-name (:full_name post-data)
                       :phone-number (:phone post-data)
                       :email-address (:email post-data))
                       :full_name :phone :email)
        lead-name (:full-name lead)
        lead-email (:email-address lead)
        email-settings (-> app/cfg :lead :thank-you-email)
        result (log-lead lead)]
    (if (:id result)
      (-> (response (generate-string {:redirect-url (str (-> app/cfg :lead :redirect-uri) "/"  (:full-name lead))}))
                (content-type "application/json"))
      {:status 500 :headers {} :body (generate-string result)})))
      
(defroutes lead-gen-routes
  (POST "/lead" {body :body} (handle-lead (parse-string (slurp body) true))))

      ;;      (render-to-response (-> (redirect (str (-> app/cfg :lead :redirect-uri) "/"  (:full-name lead))))
;;      (do
;;        (send-thank-you {:name lead-name  :email lead-email :settings email-settings})


;;  (POST "/lead" req (handle-lead (:form-parms req))))
;;(-> (response (generate-string result)) (content-type "application/json"))        
;;        (-> (redirect (str (-> app/cfg :lead :redirect-uri))))
