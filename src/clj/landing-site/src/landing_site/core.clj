(ns landing-site.core
  "Application level objects and settings"
  (:import (java.net InetAddress)))

(defn ping
  [host]
  "Return true if machine is pingable - thus up"
  (.isReachable (InetAddress/getByName host) 5000))

(defn determine-cfg-location []
  (let [env-set (System/getProperty "LSBS_CFG_DIR")]
    (if env-set
      env-set
      (str (System/getProperty "user.dir") "/website/config"))))
