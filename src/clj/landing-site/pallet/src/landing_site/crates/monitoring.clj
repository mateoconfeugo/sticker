(ns landing-site.crates.monitoring
  (:use   [pallet.api :only [plan-fn server-spec]]         
          [pallet.actions :only [package exec-script*]]
          [pallet.crate :only [defplan]]))

(defplan create-monitoring-spec
  [{:keys [config-path dist-url node-spec] :as settings}]
  (let [run-riemann-cmd "nohup /opt/riemann/bin/riemann /opt/riemann/etc/riemann.config"]
    server-spec :phases {:configure (plan-fn
                                          (package "bzip2")
                                          (exec-script* run-riemann-cmd)}))
