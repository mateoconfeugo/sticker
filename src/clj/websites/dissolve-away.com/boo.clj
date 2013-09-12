(ns cljs.user
  (:use [enfocus.core :only [from read-form at html-content content]]
        [jayq.core  :only [$ text val on prevent remove-class add-class remove]]
        [cljs.repl.browser]
        [cemerick.piggieback/cljs-repl]))

(cemerick.piggieback/cljs-repl :repl-env
                               (doto
                                   (cljs.repl.browser/repl-env :port 9000)
                                 (cljs.repl/-setup)))

(jayq.core/at (jayq.core/$ :#header_lead_full_name) (enfocus.core/content  "Ed"))
(at ($ :#header_lead_full_name) 
(js/alert "I'm ready!")
