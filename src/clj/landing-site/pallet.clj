(require
   '[pallet.core :only [group-spec node-spec ]]
   '[pallet.configure :only [compute-service defpallet]]     
   '[pallet.api :only [plan-fn]]
   '[pallet.actions :only [package]]
   '[pallet.crate.java :as java]
   '[pallet.crate.runit :as runit]
   '[pallet.compute.vmfest.service]
   '[pallet.crate.app-deploy :as app-deploy])

(def remote-jar "/home/pallet-admin/landing-site.jar")
(def local-jar  "/Users/matthewburns/github/florish-online/src/clj/landing-site/target/landing-site-0.1.0-standalone.jar")
(def app-launch-cmd  "java -jar /home/pallet-admin/landing-site.jar")
(def remote-cms-dir "/home/pallet-admin/website")
(def local-cms-dir "/Users/matthewburns/github/florish-online/src/clj/landing-site/test/site.tgz")
(def delivery-node-spec (pallet.api/node-spec :image {:image-id :java-mysql-postfix}))

(def with-landing-site  (pallet.api/server-spec
   :phases {:configure (pallet.api/plan-fn (pallet.actions/remote-file remote-jar :local-file  local-jar :user "pallet-admin"))}))

(def start-landing-site (pallet.api/server-spec
                         :phases {:configure (pallet.api/plan-fn (pallet.actions/exec-script* app-launch-cmd))}))

(def cms-resources   (pallet.api/server-spec
                      :phases {:configure (pallet.api/plan-fn (pallet.actions/remote-directory remote-cms-dir
                                                                                               :local-file  local-cms-dir
                                                                                               :owner "pallet-admin"
                                                                                               :group "pallet-admin"
                                                                                               :unpack :tar))}))

(def landing-sites (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec
                                          :extends [with-landing-site cms-resources start-landing-site]))

(defproject landing-site  :provider :vmfest :groups [landing-sites])
