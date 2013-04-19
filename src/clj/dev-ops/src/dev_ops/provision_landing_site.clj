(ns dev-ops.provision-landing-site
  (:use
   (pallet.script [lib :as lib])
   (pallet [core :only (lift group-spec converge)]
           [configure :only (compute-service defpallet)])
   ))


(defpallet
  :environment {:algorithms
                {:lift-fn pallet.core/parallel-lift
                                  :converge-fn pallet.core/parallel-adjust-node-counts}}
  :services  {
              :rs {:provider "cloudservers-us"
                   :identity "mateoconfeugo"
                   :credential "f0ec2121629538fb6c4ac10f1e279881"}})



(def landing-sites
  (group-spec "landing-site"
              :node-spec {:image {:os-family :debian}
                          :hardware {:smallest true}
                          :network {:inbound-ports [3000 3001]}}))


(defn create-landing-site []
  "build a landing site"
  (converge {landing-sites 1} :compute :rs))


(def service
  (pallet.configure/compute-service "cloudservers-us" :identity "mateoconfeugo" :credential "f0ec2121629538fb6c4ac10f1e279881"))




;;  :rackspace-cloudservers-us.endpoint "https://lon.dentity.api.rackspacecloud.com/v2.0/"


