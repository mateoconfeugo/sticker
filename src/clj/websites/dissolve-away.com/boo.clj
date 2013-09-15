(ns cljs.user
  (:use  [cljs.repl.browser]
         [cemerick.piggieback]
         [jayq.core :as jq :only [$ text val on prevent remove-class add-class remove]]))
  
;;                   [enfocus.core :only [from read-form at html-content content xpath set-data defsnippet]]))

(cemerick.piggieback/cljs-repl
 :repl-env
 (doto
     (cljs.repl.browser/repl-env :port 9000)
   (cljs.repl/-setup)))

(jayq.core/val (jayq.core/$ :#header_lead_full_name))


(em/defsnippet my-snippet "templates/carousel.html" "#doctor-referral"  []
  "h3" (enfocus.core/content "blah"))

(my-snippet {})


(ef/at "#doctor-referral" (ef/content "hello world"))

(ef/at (ef/xpath "//section[@id='doctor-referral']") (ef/content "hello world"))
(ef/at (enfocus.core/xpath "//section[@id='doctor-referral']") (enfocus.core/set-data :key "yuck") )


(jayq.core/at (jayq.core/$ :#header_lead_full_name) (enfocus.core/content  "Ed"))
(at ($ :#header_lead_full_name) )
(js/alert "I'm ready!")
