(ns dev-ops.user-account
  (:use
   [pallet.script [lib :as lib]]
   (pallet [core :only (lift group-spec)]
           [stevedore :as stevedore]
           [phase :only (phase-fn)]
           [configure :only (compute-service defpallet)])
   (pallet.action [user :only (user, group)]
                  [remote-file :only (remote-file)]
                  [exec-script :only (exec-script)])))


;;======================================================================
;; SETUP
;;======================================================================
(defpallet
  :services  {
              :data-center {:provider "node-list"
                            :environment
                            {:user {:username "mburns"
                                    :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                    :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                            :node-list [
                                        ["ops1" "flourish-ls" "166.78.153.58" :debian]]}})

(def boxes-with-databases  (compute-service :data-center))


;; PAM create home dir on user login
;; add line to /etc/pam.d/common-account
;; session    required   pam_mkhomedir.so skel=/etc/skel/ umask=0022
;; Crates
(defn manage-db-admins [session]
  "database adminstration users and groups creation"
  (-> session
      (group "dbadmin")      
      (user "ncasim" :system true, :shell "/bin/bash", :group "dbadmin")
      (exec-script "su - ncasim && exit")
      ))


;;======================================================================
;; Apply the phase's sequence of crates to the nodes of the service
;;======================================================================
(lift
 (group-spec "flourish-ls"
             :phases {
                      :configure (phase-fn
                                  (manage-db-admins))})
 :compute boxes-with-databases)
