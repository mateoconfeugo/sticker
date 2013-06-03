(ns landing-site.server.monitoring
  "Returns the group spec to provision, install and load landing site database"
  (:use [clojure.core]
        [pallet [api :only [server-spec plan-fn]
                 actions :only [remote-directory user remote-file exec-script*]]]
        [landing-site [servers.packages :only[with-bzip2]
                       config :only [monitoring-settings]]]))

(def download-riemann (server-spec
                       :phases {:install (remote-directory  (:install-dir monitoring-settings)
                                                            :url (:dist-url monitoring-settings))}))

(def riemann-user (server-spec
                   :phases {:configure (user (:user monitoring-settings)
                                             {:action :create :shell :bash :create-home 1})}))

(def riemann-cfg (server-spec
                  :phases {:settings (plan-fn
                                      (remote-file (:remote-cfg-file monitoring-settings) {:local-cfg-file monitoring-settings}))}))

(def monitoring-spec (server-spec :extends [with-bzip2 riemann-user download-rieman riemann-cfg]
                                  :phases {:run (plan-fn (exec-script* (:riemann-cmd monitoring-settings)))}))
