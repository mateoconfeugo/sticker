(ns management.dev-ops
  ^{:author "Matthew Burns"
    :doc "DEV-OPS operations that set up the management server and its dependencies
         in the appropriate manner for the deployment environment"}
  (:require [pallet.configure :refer [compute-service defpallet]]
        [clj-ssh.cli :refer [generate-keypair]]
        [clojure.string :refer [trim-newline]]))

;; TODO:  Duplicated code as that in the landing site project need to factor out
(defn create-keypair-strings
  [type size passphrase]
  "Returns vector of two elements the first bing the "
  (let [key-pair (generate-keypair type size passphrase)]
    [(trim-newline (apply str (map char (first key-pair))))
     (trim-newline (apply str (map char (last key-pair))))]))

(defn management-site-keypair []
  (create-keypair-strings :rsa 2048 ""))

(defn ssh-key-paths
  [username host tmp-dir]
  "Build up file paths for the ssh key pairs"
  (let [ssh-key-name (str username "-" host "-rsync-key")]
    {:ssh-key-name ssh-key-name
     :public-key-path (str tmp-dir "/" ssh-key-name ".pub")
     :private-key-path (str tmp-dir "/" ssh-key-name)}))

(defn pallet-release-target
  [nodes]
  (let [settings (defpallet
                   :admin-user  {:username "pallet-admin"
                                 :private-key-path "~/.ssh/id_rsa"
                                 :public-key-path "~/.ssh/id_rsa.pub"}
                   :services  {
                               :data-center {:provider "node-list"
                                             :environment
                                             {:user {:username "pallet-admin"
                                                     :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                                     :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                                             :node-list (vec nodes)}})]
    (pallet.configure/compute-service-from-config settings  :data-center {})))


(defpallet
  :admin-user  {:username "pallet-admin"
                :private-key-path "~/.ssh/id_rsa"
                :public-key-path "~/.ssh/id_rsa.pub"}
  :services  {
              :data-center {:provider "node-list"
                            :environment
                            {:user {:username "pallet-admin"
                                    :private-key-path "/Users/matthewburns/.ssh/id_rsa"
                                    :public-key-path "/Users/matthewburns/.ssh/id_rsa.pub"}}
                            :node-list [["web100" "flourish-ls" "166.78.154.230" :debian]]}})

(def qa (pallet-release-target ["dev" "flourish-ls" "198.61.220.221" :debian]))
