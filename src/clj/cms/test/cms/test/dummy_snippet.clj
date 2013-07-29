(ns test.cms.dummy-snippet
  (:use [net.cgrand.enlive-html :as html]))

(html/defsnippet test-dummy buffer *dummy-select*
  [model]
  [:h1] (html/content "huh"))