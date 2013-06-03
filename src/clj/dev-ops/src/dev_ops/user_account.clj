(ns dev-ops.user-account
  (:use
   [pallet.script.lib :as lib]
   (pallet [core :only [lift group-spec plan-fn node-spec]]
           [stevedore :as stevedore]
           [configure :only [compute-service defpallet]])
   [pallet.actions :only [user group remote-file exec-script*]]))


;;======================================================================
;; SETUP
;;======================================================================
(def release-pallet (defpallet
                      :admin-user  {:username "pallet-admin"
                                    :private-key-path "~/.ssh/id_rsa"
                                    :public-key-path "~/.ssh/id_rsa.pub"}
                      :services  {
                                  :data-center {:provider "node-list"
                                                :environment
                                                {:user {:username "mburns"
                                                        :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                                        :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                                                :node-list [
                                                            ["ops1" "flourish-ls" "166.78.153.58" :debian]]}}))

(def release-target (pallet.configure/compute-service-from-config release-pallet :data-center {}))

(defn release-landing-site [session]
  "deploy a new release to cloud"
  (-> session
      (exec-script* "touch ham")))


  
;; PAM create home dir on user login
;; add line to /etc/pam.d/common-account
;; session    required   pam_mkhomedir.so skel=/etc/skel/ umask=0022
;; Crates
(defn manage-db-admins [session]
  "database adminstration users and groups creation"
  (-> session
      (group "dbadmin")      
      (user "ncasim" :system true, :shell "/bin/bash", :group "dbadmin")
      (exec-script "su - ncasim && exit")))



;;======================================================================
;; Apply the phase's sequence of crates to the nodes of the service
;;======================================================================
(lift
 (group-spec "flourish-ls"
             :phases {
                      :configure (phase-fn
                                  (manage-db-admins))})
 :compute boxes-with-databases)

(def ham ( (lift
 (group-spec "flourish-ls"
             :phases {
                      :configure (pallet.core/plan-fn
                                  (release-landing-site))})
 :compute release-target)))

(pallet.core/node-spec tag
  :configure (pallet.core/plan-fn
              (pallet.resource.package/package "wget")))
(pallet.core/lift tag)
