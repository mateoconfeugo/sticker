(ns crates.cms
  (:use [landing-site.crates.cms]
        [landing-site.dev-ops-config :only [delivery-settings qa-release-target production-cms]]
        [pallet.api :only [plan-fn node-spec server-spec group-spec lift]]))

(def ssh-info (ssh-key-paths "pcs" "web100" "."))
(def pcs-cms-server-qa {:username "pcs"
             :app-cfg-path "/home/pcs/config/site-config.json"
             :website-dir "/home/pcs/website"
             :db-address "127.0.0.1"
             :db-password "test123"
             :db-user "root"
             :db-name "pcs"
             :monitoring-uri "127.0.0.1"
             :owner "bric201"
             :group "bric201"
             :cms-sites-root-dir "/home/bric201/landing-sites"
             :domain "patientcomfortreferral.com"
             :authorized-keys-path "/home/bric201/authorized_keys2"})

(def cms-server-spec (setup-user-on-cms (merge ssh-info pcs-cms-server-qa)))

(def pcs-cms-client-qa {:username "pcs"
             :owner "bric201"
             :group "bric201"
             :domain "patientcomfortreferral.com"
             :bin-dir "/home/pcs"})
                                           
(def cms-client-spec (server-spec :extends [(rsync-with-cms  pcs-cms-client-qa)]))



(def results (lift (group-spec "flourish-ls" :extends [user-target-spec])
                   :compute qa-release-target))

(def cms-server-results (lift (group-spec "flourish-ls" :extends [cms-server-spec])
                   :compute production-cms))

