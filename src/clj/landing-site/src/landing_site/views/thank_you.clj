(ns landing-site.views.thank-you
  (:use [net.cgrand.enlive-html]
        [flourish-common.web-page-utils :only [render-to-response]]))

(defsnippet thank-you-snippet  "templates/thank_you.html" [:body]
  [name]
  [:p#persons-name] (content name))

(deftemplate conversion-page "templates/index.html"
  [{:keys [name] :as settings}]
  [:.modal-backdrop] (content "")
  [:aside#side-lead-form] (content "")
  [:#rootwizard] (content (thank-you-snippet name))
  )

(defn render
  [name]
  "Display the thank you page that has the conversion analytics on it"
    (render-to-response (conversion-page {:name name})))
