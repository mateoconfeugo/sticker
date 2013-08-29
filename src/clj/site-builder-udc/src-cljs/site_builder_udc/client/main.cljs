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

;; ### Confirm we have remote-calling activated
;; Also notice how the remote call is namespaced to our API (`api/ping-the-api`).
(comment
(srm/rpc
  (api/ping-the-api "Testing...") [pong-response]
    (js/alert pong-response)))

;; The `rpc` macro can optionally take a keyed-body,
;; allowing you to specify error handlers.
(comment
(srm/rpc
  (api/this-is-404 "Failure") [api-response]
    :on-success (js/alert "You should never see this")
    :on-error (js/alert "Remotes correctly handle error conditions")))

