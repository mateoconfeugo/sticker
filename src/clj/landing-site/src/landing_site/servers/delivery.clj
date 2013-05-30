(ns landing-site.servers.delivery
  "Provision and install landing site optimization delivery server"
  (:use [pallet [api :only [server-spec]]]
        [landing-site.servers [cms :only[cms-spec]
                               supervise :only [supervise-spec]
                               packages :only [with-rsync with-daemontools]]]))

(def delivery-spec (server-spec (:extends [with-rsync with-daemontools app-jar cms-spec supervise-spec])))

    

