(ns landing-site.core
  "Kinda a bit of a catch all for ideas that don't have a better home"
  (:import (java.net InetAddress)))

(defn ping
  [host]
  "Return true if machine is pingable - thus up"
  (.isReachable (InetAddress/getByName host) 5000))

