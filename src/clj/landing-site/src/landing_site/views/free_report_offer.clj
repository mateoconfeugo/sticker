(ns landing-site.views.free-report-offer
  (:use [net.cgrand.enlive-html]))

(def ^:dynamic  *offer-sel* [:div.offer])
(defsnippet offer "templates/offer.html" *offer-sel*
  [offer]
  [:li#free_report_headline] (content (:headline offer))
  [:li#free_report_header_one] (content (:free_report_header_one offer))
  [:li#free_report_header_two] (content (:free_report_header_two offer))
  [:li#free_report_description] (content (:free_report_description offer)))
