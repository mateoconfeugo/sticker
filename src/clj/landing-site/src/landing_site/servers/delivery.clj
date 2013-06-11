(ns landing-site.servers.delivery
  "Provision and install landing site optimization delivery server"
  (:use [landing-site.servers.cms :only[cms-spec]]
        [landing-site.servers.supervise :only [supervise-spec]]
        [landing-site.packages :only [with-rsync with-daemontools]]
        [pallet [api :only [server-spec]]]))

(def delivery-spec (server-spec (:extends [with-rsync with-daemontools app-jar cms-spec supervise-spec])))

    

