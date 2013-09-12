(ns management.main
  "The main entry point for the client side application"
  (:require [shoreleave.common :as common]
            [shoreleave.browser.history :as history]
            [cljs.core.async :as async  :refer [timeout]]
            [flourish-common.utils.macros]
            [clojure.string :as string])            
  (:require-macros  [shoreleave.remotes.macros :as srm]
                    [cljs.core.async.macros :as m :refer [go]]
                    [flourish-common.utils.macros :refer [go-loop]]))

(defn start []
  (do
    (loop [i 0]
      (if (< i 1000)
        (do
          (go-loop
           (.log js/console (<! (timeout 1000))))
          (recur (inc i)))))

    (loop [i 0]
      (if (< i 500)
        (do
          (go-loop
           (.log js/console (<! (timeout 500))))
          (recur (inc i)))))))

(start)



