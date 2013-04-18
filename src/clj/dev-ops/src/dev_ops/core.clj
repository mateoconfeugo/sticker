(ns dev-ops.core
  (:use
   (pallet.script [lib :as lib])
   (pallet [core :only (lift group-spec)]
           [stevedore :as stevedore]
           [phase :only (phase-fn)]
           [configure :only (compute-service defpallet)])
   [leiningen.pallet-fuz]
   (pallet.action [user :only (user, group)]
                  [remote-file :only (remote-file)]
                  [exec-script :only (exec-script)]))))


(defpallet
  :services  {
              :data-center {:provider "node-list"
                            :environment
                            {:user {:username "mburns"
                                    :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                    :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                            :node-list [
                                        ["ops1" "flourish-ls" "166.78.153.58" :debian]]}})


(defn release-landing-site
  "top level function that deploys new version of application to correct destinations
   and publishes the site content to be consumed by it"
  [destinations]
  (doseq [d destinations]
    (deploy-landing-site  d)
    (publish-content d)))

(defn deploy-landing-ste [d]
  (pallet-fuz d))




