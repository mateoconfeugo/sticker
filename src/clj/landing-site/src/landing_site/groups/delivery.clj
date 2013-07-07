(ns landing-site.groups.delivery
  "Provision and install landing site optimization delivery server for user
   Setup destination for cms publishing and finally launch the landing site delivery server"
  (:use [clostache.parser :only [render-resource]]
        [landing-site.servers.delivery :only[target-delivery-spec]]
        [landing-site.dev-ops-config :only [qa-delivery-settings qa-release-target production-cms]]
        [landing-site.dev-ops :only [landing-site-keypair]]
        [landing-site.servers.cms :only[cms-client-spec]]
        [landing-site.crates.cms :only[setup-user-on-cms ]]        
        [landing-site.crates.delivery :only[launch-lsbs setup-user-on-target launch-lsbs]]
        [pallet.api :only [plan-fn node-spec server-spec group-spec lift]]
        [pallet.actions :only [remote-file remote-directory directory exec-script*]]))

(def key-pair (landing-site-keypair ))
(def pcs-cms-server-qa {:username "pcs"
                        :app-cfg-path "/home/pcs/config"
                        :website-dir "/home/pcs/website"
                        :db-address "127.0.0.1"
                        :db-password "test123"
                        :db-user "root"
                        :db-name "pcs"
                        :root-dir "/home/pcs"
                        :monitoring-uri "127.0.0.1"
                        :owner "bric201"
                        :private-key (first key-pair)
                        :public-key (last key-pair)
                        :group "bric201"
                        :cms-sites-root-dir "/home/bric201/landing-site"
                        :domain "patientcomfortreferral.com"
                        :authorized-keys-path "/home/bric201/.ssh/authorized_keys2"})


(def delivery-user-group (group-spec "flourish-ls"
                                     :extends [(setup-user-on-target (assoc  pcs-cms-server-qa :private-key (first key-pair) :public-key (last key-pair)))]))


(def delivery-cfg-group (group-spec "flourish-ls"
                                    :extends [(server-spec :phases
                                                           {:configure (plan-fn
                                                                        (do
                                                                          (try
                                                                            (directory "/home/pcs/config" :owner "pcs", :group "pcs" :mode "0700")  (catch Exception e))
                                                                          (try
                                                                            (remote-file  "/home/pcs/config/site-config.json"
                                                                                          :owner "pcs", :group "pcs" :mode "0600"
                                                                                          :action :create :force true :overwrite-changes true  :no-versioning true
                                                                                          :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/resources/site-config.json")  (catch Exception e))
                                                                                                                                                    (try
                                                                            (remote-file  "/home/pcs/config/clientconfig.json"
                                                                                          :owner "pcs", :group "pcs" :mode "0600"
                                                                                          :action :create :force true :overwrite-changes true  :no-versioning true
                                                                                          :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/resources/clientconfig.json")  (catch Exception e))



                                                                          ))})]))

(def delivery-supervise-group (group-spec "flourish-ls"
                                          :extends [(server-spec :phases
                                                                 {:configure (plan-fn
                                                                              (do
                                                                                (try
                                                                                  (directory "/home/pcs/supervise/landing-site" :owner "pcs", :group "pcs" :mode "0700")  (catch Exception e))
                                                                                (try
                                                                                  (remote-file
                                                                                   "/home/pcs/supervise/landing-site/run"
                                                                                   :owner "pcs", :group "pcs" :mode "0644"
                                                                                   :action :create :force true
                                                                                   :overwrite-changes true  :no-versioning true
                                                                                   :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/resources/run") (catch Exception e))))})]))

(def delivery-app-group (group-spec "flourish-ls"
                                    :extends [(server-spec :phases
                                                           {:configure (plan-fn
                                                                        (remote-file "/home/pcs/landing-site.jar"
                                                                                     :owner "pcs" :group "pcs"
                                                                                     :action :create :force true
                                                                                     :overwrite-changes true  :no-versioning true                                                        
                                                                                     :local-file "/Users/matthewburns/github/florish-online/src/clj/landing-site/target/landing-site-0.1.0-standalone.jar"))})]))

;;(def  launch-env {:script-env {:LSBS_CFG_DIR cfg-dir :LSBS_WEBSITE website-dir :LSBS_ROOT_DIR root-dir
;;                                 :LSBS_DB_ADDRESS db-host :LSBS_MONITORING_ADDRESS monitor-host :LSBS_DB_NAME db-name
;;                                 :LSBS_DB_USER db-username :LSBS_DB_PASSWORD db-password} })


(def delivery-start-group (group-spec "flourish-ls"
                                      :extends [(server-spec :phases
                                                             {:configure (plan-fn
                                                                          (try (exec-script* "svc -u /home/pcs/supervise/landing-site/run") (catch Exception e)))})]))


;;find website  -type d -exec chmod g+s {} \



(def delivery-result (lift delivery-user-group :compute qa-release-target))
(def delivery-result (lift delivery-cfg-group :compute qa-release-target))
(def delivery-result (lift delivery-supervise-group :compute qa-release-target))
(def delivery-result (lift delivery-app-group :compute qa-release-target))
(def cms-delivery-group-spec (group-spec "flourish-ls" :extends [cms-client-spec]))
(def cms-delivery-result (lift cms-delivery-group-spec :compute qa-release-target))
;;(def delivery-result (lift delivery-start-group :compute qa-release-target))
(def cms-group-spec (group-spec "flourish-ls" :extends [(setup-user-on-cms pcs-cms-server-qa)]))
(def cms-result (lift cms-group-spec :compute  production-cms))

(def release-delivery-qa (group-spec "flourish-ls" :extends [delivery-user-group delivery-cfg-group
                                                             delivery-supervise-group delivery-app-group
                                                             cms-delivery-group-spec]))
(def release-cms-qa (group-spec "flourish-ls" :extends [cms-group-spec]))
(def start-qa (group-spec "flourish-ls" :extends [(launch-lsbs  pcs-cms-server-qa)]))
;;(def cms-result (lift start-qa :compute  qa-release-target))


  

