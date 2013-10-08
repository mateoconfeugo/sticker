(ns test.flourish-common.db
  (:require [clojure.core :as core]
            [expectations]
            [flourish-common.db :refer [drop-database create-database drop-table drop-tables
                                        create-user-authoring-database get-user-database all-meta
                                        all map-from-db map-from-db create-management-database]]
            [me.raynes.fs :as fs :exclude [copy file]]                        
            [net.cgrand.enlive-html :as html]
            [com.ashafa.clutch.http-client]
            [com.ashafa.clutch ]))

;;================================================
;; SETUP: Test wide data
;;================================================
(def test-cfgs {:db-name "test-cms"
                :dom "<div id='test-dom'><h1>bar</h1></div>"
                :layout "<html><body><div class='uuid-1' id='host'>foo</div></body></html>"
                :xpath "/html/body/div/"
                :ls-id 1
                :base-dir "/tmp/cms/"
                :uuid 1})

;; This will serve as the data from the post
(def test-model {:xpath (:xpath test-cfgs)
                 :dom (:dom test-cfgs)
                 :layout (:layout test-cfgs)
                 :uuid (:uuid test-cfgs)
                 :base-dir (:base-dir test-cfgs)})

;;================================================
;; Site-builder dsn
;;================================================
(def test-db-spec{:classname "com.mysql.jdbc.Driver"
                  :subprotocol "mysql"
                  :subname "//127.0.0.1:3306/mydb"
                  :user "myaccount"
                  :password "secret"})