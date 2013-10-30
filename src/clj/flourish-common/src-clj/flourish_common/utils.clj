(ns flourish-common.utils)

(defn str->int [str]
  (if (re-matches (re-pattern "\\d+") str) (read-string str)))

  