(ns views.thank-you
  (:use [landing-site.views.thank-you :as thank-you]
        [expectations]
        [landing-site.core]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request]]))
    

(def output (render-to-response (thank-you/conversion-page {:name "Matt"})))
