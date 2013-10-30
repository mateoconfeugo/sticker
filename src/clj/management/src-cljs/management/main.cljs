(ns management.main
  "The main entry point for the client side application"
(:require [management.login :refer [init]]))

(defn start []
  (do
    (.log js/console "Management client site starting and prompting for login")
;;    (init)
    ))

(start)
