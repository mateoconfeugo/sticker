(ns views.host-dom
  (:require [clojure.java.io :as io])    
  (:use [landing-site.views.host-dom :as host]
        [landing-site.views.snippets]
        [expectations]
        [landing-site.config]
        [cms.site]
        [korma.core :only [defentity database insert values select where] :as kdb]
        [korma.db :only [defdb mysql]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request render-snippet]]))
    
(def cms (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))
(get-fonts cms)
(get-site-contents cms)
(get-header-image cms)
(def test-request {:request-method :get
                   :uri "/?ls-url=patientcomfortreferral.com&market_vector=1"
                   :headers {}
                   :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1 :ls-url "patientcomfortreferral.com"}})


(def market-vector-id 1)
(def      dir landing-site.config/website-dir)
(def      req {:request-method :get
           :uri "/?ls-url=patientcomfortreferral.com&market_vector=1"
           :headers {}
           :params {:adnetwork 1, :campaign 1, :adgroup 1, :listing "A" :market_vector 1 :ls-url "patientcomfortreferral.com"}})
(def          matrix-id 1)
        ;;        domain (if-let [d (-> req :params :ls-url)] d (host-of (:server-name req)))
(def        domain (-> req :params :ls-url))
(def         mv-id (or market-vector-id (or (-> req :params :market_vector) (get-market-vector domain dir matrix-id))))
(def        cms (new-cms-site {:webdir dir :market-vector-id mv-id :domain-name domain}))
(def        menu (:drop_down_menu (first (get-site-menu cms))))
(def        pages (get-site-contents cms))
(def        css (get-css cms))
(def css-path (str  dir  "/" domain "/site/landing_site/1/1.css"))
(slurp css-path)
(def lines (line-seq (io/reader "/Users/matthewburns/github/florish-online/src/clj/landing-site/website/patientcomfortreferral.com/site/landing_site/1/1.css")))

(use 'clojure.java.io)
(with-open [rdr (reader css-path)]
  (doseq [line (line-seq rdr)]
        (println line)))

(doseq [l lines] (println l))

(println css)
(def        header-image-path (str dir "/" domain  (get-header-image cms)))
(def        fonts (get-fonts cms))
(def        num_pages (count pages))
(def        page_num (range 0 num_pages))
(def        pages (reverse (map #(assoc %1 :order %2)  pages page_num)))
(def opts    {:site-name "MarketWithGusto.com" :pages pages :menu-data menu
              :css css :fonts fonts :header-image header-image-path})
(:header-image opts)

(index-with-webapp-pages opts)

(-> test-request :params :market_vector)

(host/render test-request 1)

(def domain (-> test-request :params :ls-url))


(def html-output (host/render test-request 1))
(def pages (assemble-site-files root-dir 1))
(def pages-contents (populate-contents (str website-dir "/" domain "/site") pages 1 ))
(def menu (:drop_down_menu (first (get-site-menu cms ))))
(get-site-contents cms token)

(first pages-contents)
(def output (render-to-response (host/index-with-webapp-pages {:site-name "MarketWithGusto.com" :pages pages-contents :menu-data menu})))
(render-snippet offer "blah")
(offer {})