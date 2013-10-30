(ns heat-mapping.client
  "The main entry point for the client side application"
  (:require-macros [shoreleave.remotes.macros :as srm :refer [rpc]])
  (:require [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [shoreleave.common :as common]
            [shoreleave.browser.history :as history]))

(defn ^:export send-coords [x y]
  "Sends the mouse x y coordinates to the server"
  (srm/rpc
   (api/mouse-position x y)
   [resp]
   (.log js/console (str (:x resp) (:y resp) (:z resp)))))

(defn ^:export landing-site-heat-map [landing-site-id]
  (srm/rpc
   (api/heat-mapping/fetch-map landing-site-id) [resp]
   (js/alert (str "Coords: "  (:data resp)))))

