(ns dev-opts.infrastructure
  "This library builds the flourish network of computing resources to accomplish the functional business"
  (:use
   (pallet.script [lib :as lib])
   (pallet [api :only (server-spec plan-fn)]
           [core :only (lift group-spec)]
           [repl :only (use-pallet)]
           [stevedore :as stevedore]
           [phase :only (phase-fn)]
           [configure :only (compute-service defpallet)])
   (pallet.action [user :only (user, group)]
                  [remote-file :only (remote-file)]
                  [exec-script :only (exec-script)]
                  [:only (package)])
   (pallet.crate [automated-admin-user :only (automated-admin-user)])))

;;======================================================================
;; SETUP
;;======================================================================
(defpallet
  :services  {
              :data-center {:provider "cloudservers"
                            :environment
                            {:user {:username "mburns"
                                    :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                    :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                            :node-list [
                                        ["ops1" "flourish-ls" "166.78.153.58" :debian]]}})

(def boxes-with-databases  (compute-service :data-center))

;;======================================================================
;; BASE COMPONENTS:  
;;======================================================================
(defn hookup-nfs [session]
  "hookup the common filesystem resourcs"
  (-> session
      (symbolic-link "original/path" "link/path")))

(defn common-user-groups [sessions]
  "create the necessary unix user groups the system uses"
  (-> session
      (group "flourish")))

(defn common-users [sessions]
  "create the necessary unix users"
  (-> session
      (user "ad-delivery")))

(defn common-services [sessions]
  "create the necessary unix user groups the system uses"
  (-> session
      (service "flourish-services")))

(defn common-cron [sessions]
  "create the necesssary cronjobs"
  (-> session
      (service "flourish-services")))

(def flourish-online
  (compute-service "provider" :identity my-user :credential my-password))

(def common-configuration
  (server-spec
   :phases {:configure (plan-fn (package "wget"))}))

(def base-debian-packages
  (server-spec
   :phases {:configure (plan-fn (package "wget"))}))

(def base-perl-packages
  (server-spec
   :phases {:configure (plan-fn (package "curl"))}))

;;======================================================================
;; Server specifications: These are the servers that make up flourish
;; onlines physical presence
;; Things to specify
;; 1: Users and Groups
;; 2: Application and 3rd Party Packages
;; 3: Cronjobs
;; 4: Services
;; 5: Files
;; 6: Symbolic Links
;;======================================================================
;; Base server from which all other inherit 
(def server-base
  (server-spec
   :extends [base-debian-packages base-perl-packages common-configuration]
   :phases {:configure (plan-fn
                        (package "curl")
                        (common-user-groups)
                        (common-users)
                        (common-services)
                        (hookup-nfs)
                        (common-cron)
                        )}))
(def website
  (group-spec "web"
              :extends [server-base]
              :phases {:bootstrap (phase-fn (automated-admin-user))}              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [80]}}))

(def management
  (group-spec "web"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [8080]}}))

(def landing-sites
  (group-spec "landing-site"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [3000 3001]}}))

(def verticals
  (group-spec "vertical-site"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [4002 4003]}}))

(def database 
  (group-spec "database"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [3306 5432]}}))
(def balancer
  (group-spec "balancer"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [80 8080]}}))

(def qa-testing
  (group-spec "qa"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [6000 6001 6002 6003]}}))

(def dev-ops-server
  (group-spec "dev-ops"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [6000 6001 6002 6003]}}))

(def ad-delivery-server
  (group-spec "adnetwork"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [7000 7002]}}))

(def ad-click-server
  (group-spec "adnetwork"
              :extends [server-base]              
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [5000 5001]}}))

;; results contains a large amount of information, including the
;; logs resulting from Pallet running all the scripts on the nodes on
;; your behalf (in this case, to create this admin user).
(def results
  (converge {
             landing-sites 1
             balancer 1
             database 1
             management 1
             verticals 1
             qa-testing 1
             dev-ops-server 1
             } :compute flourish-online))

)

;;(:all-nodes results)
;;(lift webserver :compute flourish-online)

(comment
(def results
  (converge {
             landing-sites 0
             balancer 0
             database 0
             management 0
             verticals 0
             qa-testing 0
             dev-ops-server 0
             } :compute flourish-online))
)

