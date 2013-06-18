(ns landing-site.crates.cms
  (:use [clostache.parser :only [render-resource]]
        [landing-site.dev-ops :only [ssh-key-paths]]
        [landing-site.dev-ops-config :only [delivery-settings qa-release-target production-cms]]
        [pallet.api :only [plan-fn node-spec server-spec group-spec lift]]
        [pallet.actions :only [remote-directory directory exec-script* remote-file]]
        [pallet.crate :only [defplan]]))


(defplan create-cms-lsbs
  [{:keys [cms-site-tgz-url cms-remote-dir owner group] :as settings}]
  (server-spec :phases {:configure (plan-fn
                                    (remote-directory cms-remote-dir
                                                      :url cms-site-tgz-url
                                                      :owner owner :group group :mode "0644"
                                                      :action :create :force true
                                                      :unpack :tar))}))
;;        crontab-edit-cmd (str "crontab  -u " username " -e  "  crontab-entry)   
(defplan rsync-with-cms
  [{:keys [domain-name owner group bin-dir username] :as settings}]
  "keep production website upto date with the most recently published version"
  (let [crontab-entry (str "* * * * *  /home/" username "/bin/rsync-site-cms")
        crontab-edit-cmd (str "crontab -u  " username " -l ; echo  '" crontab-entry "' | crontab -u " username "  -")]
    (server-spec :phases {:settings (plan-fn
                                     (do
                                       (try (exec-script* crontab-edit-cmd) (catch Exception e))
                                       (try (directory bin-dir :action :create :owner username :group username) (catch Exception e))
                                       (try (remote-file
                                        (str bin-dir "/rsync-site-cms")
                                        :owner username :group username :mode "0744"
                                        :action :create :force true
                                        :overwrite-changes true :no-versioning true
                                        :content (render-resource "templates/rsync-cms-site-content.tmpl"
                                                                  {:domain-name domain-name
                                                                   :username username})) (catch Exception e))
                                       ))})))

;; remove-key-if-exists (str "egrep -v '" public-key "' /home/bric201/.ssh/authorized_keys2 > /home/bric201/.ssh/tmpkeys && mv /home/bric201/.ssh/tmpkeys /home/bric201/.ssh/authorized_keys2")
;;                                        (exec-script* remove-key-if-exists)                                          

(defplan setup-user-on-cms
  [{:keys [username domain public-key cms-sites-root-dir owner authorized-keys-path group] :as settings}]
  "create a directory for the user and domain
   copy that users host domain public key into authorized_keys2 file
   removing a previous key from domain if it exists"
  (let [auth-file-cmd (str "echo " public-key " >> " authorized-keys-path)]
    (server-spec :phases {:configure (plan-fn
                                      (do
                                        (directory (str cms-sites-root-dir "/" username "/" domain)
                                                   :action :create  :owner "bric201" :group "bric201" :mode "0700")
                                        (exec-script* auth-file-cmd)))})))









                                    