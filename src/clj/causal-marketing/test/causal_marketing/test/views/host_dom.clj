(ns causal-marketing.test.views.host-dom
  (:use   [cheshire.core :only (parse-string)]
          [clojure.test]
          [ring.mock.request  ]
          [net.cgrand.enlive-html :as html]          
          [causal-marketing.views.host-dom :as host]
          [causal-marketing.controllers.site :as site]          
          ))

(def path (str (System/getProperty "user.dir") "/website/market_vector/default-demo/default-demo.json"))

(def json (slurp path))
(def site-data  (cheshire.core/parse-string json true))
(def menu-data (:drop_down_menu (first (cms.site/get-site-menu site/cms "default-demo"))))
(def menu   (first menu-data))
(def menu-title (:drop_down_menu_name menu))
(def data (:menu_item  (first menus)))
(menu-model {:title menu-title :data data} menu-item-model)

(nav-bar {:title "SMig" :menu-data menu})

(use `causal-marketing.controllers.site)
(def pages (cms.site/get-site-contents site/cms "default-demo"))
(host/index {:site-name "Bob" :pages pages :menu-data menu-data})

(def response-html (host/render "default-demo" cms))

(first response-html)
menus

(for [m (:menu_item (first menus))] (pprint m))
(host/menu-item {:menu_item_text "Landing Site Builder"})





