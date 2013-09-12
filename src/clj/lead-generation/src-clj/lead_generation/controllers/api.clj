(ns lead-generation.controllers.api
  "API controller for lead generation client")

(defn list-leads [{:keys [user landing-site-id filter range]}]
  {:leads [[{:name "foo" :phone "5555555555"}
            {:name "bar" :phone "5555555555"}]]})


