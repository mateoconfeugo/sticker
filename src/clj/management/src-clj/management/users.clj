(ns management.users
  (:require [cemerick.friend.credentials :refer [ hash-bcrypt]]))

(def users (atom {"friend" {:username "matthewburns@gmail.com"
                            :password (hash-bcrypt "clojure")
                            :pin "1234" ;; only used by multi-factor
                            :roles #{::user}}
                  "friend-admin" {:username "matthewburns@gmail.com"
                                  :password (hash-bcrypt "clojure")
                                  :pin "1234" ;; only used by multi-factor
                                  :roles #{::admin}}}))

(derive ::admin ::user)
