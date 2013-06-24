(ns cms.site
  "Interacting with cms consturcted site
   the site data is made up of being able to
   access html page files, market vector and matrix json file"
  (:require [clojure.java.io :as io])  
  (:use [cheshire.core :only (parse-string parse-stream)]
        [me.raynes.fs :only(directory?) :as fs]))

;; BACK-END HELPER FUNCTIONS
(defn str->int [str]
  (if (re-matches (re-pattern "\\d+") str) (read-string str)))

(defn regex-file-seq
  "Lazily filter a directory based on a regex."
  [re dir]
  (filter #(re-find re (.getPath %)) (file-seq dir)))

(defn get-header [page]
  (:copy (first (-> page :inset ))))

(defn- wildcard-filter
  "Given a regex, return a FilenameFilter that matches."
  [re]
  (reify java.io.FilenameFilter
    (accept [_ dir name] (not (nil? (re-find re name))))))

(defn directory-list
  "Given a directory and a regex, return a sorted seq of matching filenames."
  [dir re]
  (sort (.list (clojure.java.io/file dir) (wildcard-filter re))))

(defn get-site-id
  [base-dir  market-vector-id]
  "Retreive the id of the site the market-vector-id is linked with"
  (let [mv-path (str  base-dir  "/market_vector/" market-vector-id "/" market-vector-id ".json")]
    (str->int (name (first (keys ((keyword "landing_site") (parse-string (slurp mv-path) true))))))))

(defn assemble-site-files [base-dir landing-site-id]
  "Determines the files that make up a site"  
  (let [dir-path (str base-dir "/landing_site/" landing-site-id)
       directory (clojure.java.io/file dir-path)
       sorted-files (directory-list directory #".*\.html")]
         (map #(str base-dir "/landing_site/" landing-site-id "/" %) sorted-files)))
;;        html-files (regex-file-seq #".*\.html" directory)


(defn get-site-data
  [base-dir  market-vector-id]
  "retreive data structure that decribes the site"
  (let [mv-path (str  base-dir  "/market_vector/" market-vector-id "/" market-vector-id ".json")
        data (get (parse-string (slurp mv-path) true) (keyword "landing_site"))]
    (get  data (first (keys data)))))

(defn populate-contents [base-dir files market-vector-id]
  "gets all the content for the single page web app view of the site"  
  (let [f (filter fs/file? files)
        pages (map slurp f)
        headers (map get-header (:page (get-site-data  base-dir market-vector-id)))]
    (map (fn [x] {:header (first x) :contents (last x)}) (zipmap headers pages))))

(defn get-market-vector
  [domain-name website-dir  matrix-id]
  (:market_vector_id (first (parse-string (slurp (str website-dir "/" domain-name "/site/market_matrix/" matrix-id "/" matrix-id ".json")) true))))

(defn cms-css
  [base-dir landing-site-id]
  "Retrieve the landing site css generated in the cms"
  (slurp (str base-dir "/landing_site/" landing-site-id "/" landing-site-id ".css")))

(defn cms-fonts
  "Get the html5 fonts"
  [base-dir  landing-site-id]
  (let [fonts (parse-stream (io/reader (str  base-dir  "/landing_site/" landing-site-id "/" landing-site-id ".json")) true)]
    (map :font_href (:font_link (first (:fonts fonts))))))


;; INTERFACE SPECIFICATION
(defprotocol CMS-Site
  "Site content file lookup interface"
  (get-css [this]
    "Gets the css for the site")
  (get-fonts [this]
    "Gets the css for the site")
  (get-site-contents [this]
    "Gets all the pages and prepares them for the view")
  (show-settings [this])
  (get-site-menu [this]
    "Retrieves the menu configuration for the site"))

;; IMPLEMENTATION CONSTRUCTOR


(defn new-cms-site
  [{:keys[domain-name market-vector-id webdir] :as settings}]
  (reify CMS-Site

    (get-fonts
      [this]
      (let [base-dir (str webdir "/" domain-name "/site")
            landing-site-id (get-site-id base-dir market-vector-id)]
        (cms-fonts base-dir landing-site-id)))

    (get-css
      [this]
      (let [base-dir (str webdir "/" domain-name "/site")
            landing-site-id (get-site-id base-dir market-vector-id)]
        (cms-css base-dir landing-site-id)))

    (show-settings
      [this]
      settings)

    (get-site-contents
      [this]
      (let [base-dir (str webdir "/" domain-name "/site")
            landing-site-id (get-site-id base-dir market-vector-id)
            page-files (assemble-site-files base-dir landing-site-id)]
        (populate-contents base-dir page-files market-vector-id)))

    (get-site-menu
      [this]
      (let [base-dir (str webdir "/" domain-name "/site")]
        (:site_nav_header (first (:single_page_webapp (get-site-data base-dir  market-vector-id))))))))


