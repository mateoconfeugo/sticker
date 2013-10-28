(ns leiningen.new.landing-site
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [clojure.edn]))

(def render (renderer "landing-site"))

(defn landing-site
  "Creates the custom landing site web server for a user-account"
  [name resource-model-uri]
  (let [resource-model (clojure.edn/read-string (slurp resource-model-uri))
        data (assoc resource-model :name name :sanitized (name-to-path name))]
    (->files data
             ["resources/templates/head.html" (render "head.html" data)]
             ["resources/templates/navigation.html" (render "navigation.html" data)]
             ["resources/templates/carousel_banner.html" (render "carousel_banner.html" data)]
             ["resources/templates/dropdown_lead_modal.html" (render "dropdown_lead_modal.html" data)]
             ["resources/templates/free_referral_offer.html" (render "free_referral_offer.html" data)]
             ["resources/templates/header.html" (render "header.html" data)]
             ["resources/templates/inline_lead_form.html" (render "inline_lead_form.html" data)]
             ["resources/templates/scripts.html" (render "scripts.html" data)]
             ["resources/templates/index.html" (render "index.html" data)]
             ["resources/templates/top_banner.html" (render "top_banner.html" data)]
             ["resources/templates/thc_logo.html" (render "thc_logo.html" data)]
             ["resources/templates/market_message.html" (render "market_message.html" data)]
             ["resources/templates/footer.html" (render "footer.html" data)]
             ["resources/templates/lead_modal.html" (render "lead_modal.html" data)]
             ["resources/templates/carousel.html" (render "carousel.html" data)]
             ["resources/templates/release.html" (render "release.html" data)]
             ["resources/templates/dissolve-away.html" (render "dissolve-away.html" data)]
             ["resources/templates/thank_you.html" (render "thank_you.html" data)]
             ["README.md" (render "README.md" data)]
             ["project.clj" (render "project.clj" data)]
             ["resources/market_matrix.edn" (render "market_matrix.edn" resource-model)]
             ["resources/config.edn" (render "config.edn" resource-model)]
             ["src-cljs/{{sanitized}}/main.cljs" (render "main.cljs" data)]
             ["src-cljs/{{sanitized}}/layout_manager.cljs" (render "layout_manager.cljs" data)]
             ["src-cljs/{{sanitized}}/repl.cljs" (render "repl.cljs" data)]
             ["src-clj/{{sanitized}}/handler.clj" (render "handler.clj" data)]
             ["src-clj/{{sanitized}}/server.clj" (render "server.clj" data)]
             ["src-clj/{{sanitized}}/routes.clj" (render "routes.clj" data)]
             ["src-clj/{{sanitized}}/config.clj" (render "config.clj" data)]
             ["src-clj/{{sanitized}}/controllers/api.clj" (render "api.clj" data)]
             ["src-clj/{{sanitized}}/controllers/site.clj" (render "site.clj" data)]
             ["src-clj/{{sanitized}}/models/landing_sites.clj" (render "landing_sites.clj" data)]
             ["src-clj/{{sanitized}}/models/market_matrices.clj" (render "market_matrices.clj" data)]
             ["src-clj/{{sanitized}}/models/market_vectors.clj" (render "market_vectors.clj" data)]
             ["src-clj/{{sanitized}}/views/host_dom.clj" (render "host_dom.clj" data)])))
