(ns groups.database
  (:require  [korma.core])
  (:use [expectations]
        [landing-site.config :only[db-settings]]
        [landing-site.core :only[ping]]
        [landing-site.crates.database]
        [korma.db :only [defdb mysql]]
        [pallet.api :only [group-spec converge lift node-spec]]
        [pallet.configure :only [compute-service]]))

(def service (pallet.configure/compute-service (:provider db-settings)))
(def test-node-spec (node-spec :image {:image-id :java-mysql-postfix}))

(def test-database-group (group-spec (:group-spec-name db-settings)
                                     :extends[(create-database-spec db-settings)]
                                     :node-spec test-node-spec))

(def db-nodes (future (converge {test-database-group 1} :compute service)))
(def db-node  ((first (@db-nodes :targets)) :node))
(def db-ip (.primary-ip db-node))

(expect true (ping db-ip))
(converge {test-database-group 0} :compute service)
;; TODO add some tests to see if you can inserta and remove stuff from database
(defdb db (mysql {:db (:db-name  db-settings) :user (:db-user db-settings) :password (:db-password db-settings) :host db-ip}))
(korma.core/defentity lead_log (database db))
(def good-test-lead {:full_name "Cal Mee" :email "cal@gmail.com" :phone "8182546028"})
(def insert-result {:id (:GENERATED_KEY (korma.core/insert lead_log (korma.core/values [good-test-lead])))})



