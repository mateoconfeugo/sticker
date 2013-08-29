(ns site-builder-udc.client.main
  "The main entry point for the client side application"
  (:require [shoreleave.common :as common]
            [shoreleave.browser.history :as history]
            [site-builder-udc.client.flows]) ;; this wires the application up
  (:require-macros [shoreleave.remotes.macros :as srm]))

(def query-args (common/query-args-map))
(def hash-args (common/hash-args-map))

;; Integrate history/back-button to the search
;For example:
;    (history/navigate-callback #(process-search (subs (:token %) 4) false))
;Where process-search is the main action to take when processing the page/url
;; ### Browser REPL
;; If you add a `repl` as a query-string arg, even on the live Baseline,
;; You can remotely interact with the site from the local REPL
;; Visit: `http://127.0.0.1:8080/test?repl=yes#q=something+else`
(common/toggle-brepl query-args :repl)
