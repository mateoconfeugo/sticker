(ns dev-ops.release-ls
  "Node defintions for release-ls"
  (:require
   [pallet.core :only [group-spec node-spec ]]
   [pallet.configure :only [compute-service defpallet]]     
   [pallet.api :only [plan-fn]]
   [pallet.actions :only [package]]
   [pallet.crate.java]))
   [pallet.compute.vmfest.service]))

(def release-local (pallet.configure/compute-service :vmfest))
;;(def release-target (pallet.configure/compute :data-center ))

(def with-tree
  (pallet.api/server-spec
   :phases {:configure (pallet.api/plan-fn (pallet.actions/package "tree"))}))

(def test-node-spec    (pallet.api/node-spec :image {:image-id :beta}))

(def debian-laptop-group (pallet.api/group-spec "debian-vbox"
                                     :node-spec test-node-spec
                                     :extends [(pallet.crate.java/server-spec {}) with-tree]))

;;(pallet.api/lift  debian-laptop-group :compute release-local)
(def virtual-box (pallet.api/converge  {debian-laptop-group 1} :compute release-local))
(def test-target-node  ((first (virtual-box :targets)) :node))
(def target-ip (.primary-ip test-target-node))
(def virtual-box (pallet.api/converge  {debian-laptop-group 0} :compute release-local))
