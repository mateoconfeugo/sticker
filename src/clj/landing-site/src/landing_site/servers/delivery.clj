(ns landing-site.servers.delivery
  "Provision and install landing site optimization delivery server"
  (:use [landing-site.servers.cms :only[cms-client-spec]]
        [landing-site.servers.supervise :only [supervise-spec]]
        [landing-site.servers.packages :only [with-rsync with-daemontools with-ftp-client]]
        [pallet.api :only [server-spec]]))

(def target-delivery-spec (server-spec :extends [with-daemontools with-rsync with-ftp-client supervise-spec]))

(comment
                                                 user-target-spec cms-client-spec supervise-spec 
                                                 lsbs-app-jar)


