(require '[landing-site.groups.delivery])
;;(def dev-qa-group (group-spec "dev-qa-landing-site" extends [base-db-group-spec base-delivery-group-spec]))
;;(defproject landing-site-delivery-qa :groups [release-delivery-qa])
(defproject landing-site-cms-qa :groups [landing-site.groups.delivery/cms-group-spec])

