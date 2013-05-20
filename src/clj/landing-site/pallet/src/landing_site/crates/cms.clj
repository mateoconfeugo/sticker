(ns landing-site.crates.cms
  (:use   [pallet.api :only [plan-fn server-spec]]         
          [pallet.actions :only [remote-directory-directory]]
          [pallet.crate :only [defplan]]))

(defplan create-cms-lsbs
  [{:keys [local-dir-name remote-dir-name owner group] :as settings}]
  (server-spec :phases {:configure (plan-fn
                                    (remote-directory remote-dir-name :local-file
                                                      local-dir-name :owner owner
                                                      :group group :unpack :tar))}))
