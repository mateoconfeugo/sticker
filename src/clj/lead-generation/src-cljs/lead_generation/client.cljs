(ns lead-generation.client
  "The main entry point for the client side application"
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]])
  (:require [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))

(defn ^:export send-lead  [lead_full_name lead_email lead_phone]
  (srm/rpc
   (api/insert-lead lead_full_name lead_email lead_phone)
   [resp]
   resp))
