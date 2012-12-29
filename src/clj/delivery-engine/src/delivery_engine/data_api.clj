(ns delivery-engine.data-api
  (:use [ring.util.servlet])
  (:import (javax.servlet.http  HttpServletRequest HttpServletResponse)))

(defrecord Request [server-port server-name remote-addr uri query-string scheme request-method
                    headers content-type content-length character-encoding ssl-client-cert body])

(defn build-request  [^HttpServletRequest request]
  (map->Request  build-request-map request))


(defrecord Response [header code body])
(defrecord Account []);
(defrecord Contact []);
(defrecord BusinessActor []);
(defrecord BusinessRole []);
(defrecord BillableEntity []);
(defrecord Group [client-ip date method uri http-code number-bytes referer agent]);
(defrecord Set [client-ip date method uri http-code number-bytes referer agent]);
(defrecord Population [client-ip date method uri http-code number-bytes referer agent]);
(defrecord Listing [uri])
(defrecord Partner [id name status ])
(defrecord Feed [id partner-id tag-name uri])
(defrecord Feed-Config [total budgeted blackout])
(defrecord Blackout-Schedule[schedule])
(defrecord Query-Delivery-Node [hostname ip status])
(defrecord Log-Entry [line])
(defrecord Log-Record [client-ip date method uri http-code number-bytes referer agent])
(defrecord Query [keyword level id] )
(defrecord Query-Summary [level id queries-received])
(defrecord Usage-Detail [feed-id level level-id query])
(defrecord Usage-Count [feed-id level level-id count])
(defrecord Usage-Summary [feed-id level level-id count node])
(defrecord Node-Usage-Summary [node-id level level-id queries-received])
(defrecord Cluster-Usage-Summary [nodes level level-id total-queries-received])
(defrecord Budget-Item [feed-id level-id level allotment current-count])


(comment
(defprotocol DeliveryEngine
  "Object used to create listings for users to click on and get redirected to advertiser page"
  (listings [this keyword token]
    "Concurrently download the listing responses from feed partner and query database for local ads returning unique listings that have sufficient bid for the keyword")
  (search [this request]
    "query the system for listing with the request")
  (show-settings [this]
    "show the settings")
  )
)