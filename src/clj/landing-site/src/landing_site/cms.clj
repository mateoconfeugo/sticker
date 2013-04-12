(ns landing-site.cms
  "Interacting with cms consturcted site"
  (:use [cheshire.core :only (parse-string)]
        [me.raynes.fs :only(directory?) :as fs]))

;; BACK-END HELPER FUNCTIONS
(defn str->int [str]
  (if (re-matches (re-pattern "\\d+") str) (read-string str)))

(defn get-header [page]
  (:copy (first (-> page :inset ))))

(defn get-landing-site-id [market-vector base-path]
  "Retreive the id of the landing site the market vector is linked with"
  (let [mv-path (str  base-path  "/market_vector/" market-vector "/" market-vector ".json")]
    (str->int (name (first (keys (:landing_site (parse-string (slurp mv-path) true))))))))

(defn assemble-site-files [landing-site-id base-path]
  "Determines the files that make up a landing site"  
  (let [directory (clojure.java.io/file (str base-path "/landing_site/" landing-site-id))]
    (file-seq directory)))

(defn get-site-data [market-vector base-path]
  "retreive data structure that decribes the site"
  (let [mv-path (str  base-path  "/market_vector/" market-vector "/" market-vector ".json")
        data (:landing_site (parse-string (slurp mv-path) true))
        id (name (first (keys (:landing_site (parse-string (slurp mv-path) true)))))]
    ((keyword id) data)))

(defn populate-contents [files market-vector base-path]
  "gets all the content for the single page web app view of the site"  
  (let [f (filter fs/file? files)
        pages (map slurp f)
        headers (map get-header (:page (get-site-data market-vector base-path)))]
    (map (fn [x] {:header (first x) :contents (last x)}) (zipmap headers pages))))

;; INTERFACE SPECIFICATION
(defprotocol CMS-Site
  "Site content file lookup interface"
  (get-site-contents [this market-vector]
    "Gets all the pages and prepares them for the view"))

;; IMPLEMENTATION CONSTRUCTOR
(defn new-cms-site
  [{:keys[base-path] :as settings}]
  (let [one 1]
    (reify CMS-Site
      ;; PROTOCOL METHOD IMPLEMENTATIONS    
      (get-site-contents [this market-vector]
        (let [landing-site-id (get-landing-site-id market-vector base-path)
              page-files (assemble-site-files landing-site-id base-path)]
          (populate-contents page-files market-vector base-path))))))
