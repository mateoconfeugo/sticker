;*CLJSBUILD-REMOVE*;
(ns flourish-common.crossover.macros)

(defmacro go-loop [& body]
  `(cljs.core.async.macros/go
     (while true
       ~@body)))
