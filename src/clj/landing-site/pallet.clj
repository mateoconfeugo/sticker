(use
 '[pallet.configure :only [compute-service defpallet]]     
 '[pallet.api :only [group-spec node-spec server-spec plan-fn]]
 '[pallet.compute.vmfest.service]
 '[landing-site.config :only [db-settings]]
 '[landing-site.groups.database :only [db-group-spec]]
 '[landing-site.groups.delivery :only [delivery-group-spec]])

(def dev-qa-group (group-spec "dev-qa-landing-site" extends [base-db-group-spec base-delivery-group-spec]))

(defproject landing-site-dev-qa :provider (:provider db-settings) :groups [dev-qa-group])
