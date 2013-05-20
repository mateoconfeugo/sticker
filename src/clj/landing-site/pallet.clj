(require
 '[pallet.configure :only [compute-service defpallet]]     
 '[pallet.api :only [group-spec node-spec server-spec plan-fn] :as api]
 '[pallet.compute.vmfest.service]
 '[landing-site.config :only [db-settings]]
 '[landing-site.groups.database :only [base-db-group-spec]]
 '[landing-site.groups.delivery :only [base-delivery-group-spec]])

(def dev-qa-group (group-spec "dev-qa-landing-site" extends [base-db-group-spec base-delivery-group-spec]))

(defproject landing-site-dev-qa :provider (:provider db-settings) :groups [dev-qa-group])
