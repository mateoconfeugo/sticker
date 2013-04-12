(ns flourish-common.config-test
  (:use clojure.test
        flourish-common.config))

(def root-dir (str "/Users/matthewburns/github/florish-online"))
;;(def root-dir (System/getenv "FLOURISH_ROOT_DIR"))

(deftest test-config-from-file
  (let [cfg (new-config {:cfg-file-path (str root-dir "/conf/system_config.json")})
        {:keys [host port db-user db-password db-name]} (-> (read-config cfg) :delivery-engine :publish-database)]
    (is (= db-password "test123"))))
    
    
