(require
   '[pallet.core :only [group-spec node-spec ]]
   '[pallet.configure :only [compute-service defpallet]]     
   '[pallet.api :only [plan-fn]]
   '[pallet.actions :only [package]]
   '[pallet.crate.java :as java]
   '[pallet.crate.runit :as runit]
   '[pallet.compute.vmfest.service]
   '[pallet.crate.app-deploy :as app-deploy])

(def release-local (pallet.configure/compute-service :vmfest))
;;(def release-target (pallet.configure/compute :data-center ))

(def with-tree
  (pallet.api/server-spec
   :phases {:configure (pallet.api/plan-fn (pallet.actions/package "tree"))}))

(def with-landing-site
  (pallet.api/server-spec
   :phases {:configure (pallet.api/plan-fn (pallet.actions/remote-file
                                            "/home/pallet-admin/landing-site.jar"
                                            :local-file  "/Users/matthewburns/github/florish-online/src/clj/dev-ops/target/dev-ops-0.1.0-SNAPSHOT-standalone.jar" :user "pallet-admin"))}))

(def start-landing-site
    (pallet.api/server-spec
   :phases {:configure (pallet.api/plan-fn (pallet.actions/exec-script*  "java -jar /home/pallet-admin/landing-site.jar &"))}))

(def with-runit
  (runit/server-spec {:user "pallet-admin"
                      :group "pallet-admin"
                      :owner "pallet-admin"}))

              ;;           :artifacts {:from-lein [{:path "dev-ops-0.1.0-SNAPSHOT.jar"
(def with-app-deploy
  (let [cfgs {:user "pallet-admin"
              :app-root "home/pallet-admin"
              :run-command "java -jar /home/pallet-admin/dev-ops-0.1.0-SNAPSHOT-standalone.jar"

              :artifacts {:from-lein [{:path "dev-ops.jar"
                                       :project-path "/Users/matthewburns/github/florish-online/src/clj/dev-ops/target/dev-ops-0.1.0-SNAPSHOT-standalone.jar"}]}}]
    (app-deploy/server-spec cfgs :instance-id :landing-site-delivery)))



(def delivery-node-spec (pallet.api/node-spec :image {:image-id :java-mysql-postfix}))

(def landing-sites (pallet.api/group-spec "landing-sites" :node-spec delivery-node-spec
                                          ;;                                          :extends [with-tree with-runit with-app-deploy]))
                                          :extends [with-landing-site start-landing-site]))

(defproject dev-ops  :provider :vmfest :groups [landing-sites])