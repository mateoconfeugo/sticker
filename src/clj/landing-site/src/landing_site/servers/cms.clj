(ns landing-site.servers.cms
  "Handle interactions with the cms in the system"
  (:use [clojure.core]
        [landing-site.crates.cms :only [create-cms-lsbs setup-user-on-cms rsync-with-cms]]
        [landing-site.dev-ops-config :only [delivery-settings]]
        [pallet.api :only [server-spec]]))


;;(create-cms-lsbs delivery-settings)

(def pcs-cms-client-qa {:username "pcs"
             :owner "pcs"
             :group "pcs"
             :domain-name "patientcomfortreferral.com"
             :bin-dir "/home/pcs/bin"})

                                           
(def cms-client-spec (server-spec :extends [(rsync-with-cms  pcs-cms-client-qa)]))
