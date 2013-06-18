(ns cms.site
  "Interacting with cms consturcted site
   the site data is made up of being able to
   access html page files, market vector and matrix json file"
  (:use [cheshire.core :only (parse-string)]
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

(defn get-site-id
  [{:keys [base-path rel-path site-tag token]}]
  "Retreive the id of the site the token is linked with"
  (let [mv-path (str  base-path  "/" rel-path "/" token "/" token ".json")]
    (str->int (name (first (keys ((keyword site-tag) (parse-string (slurp mv-path) true))))))))

(defn assemble-site-files [base-path  site-tag site-name]
  "Determines the files that make up a site"  
  (let [directory (clojure.java.io/file (str base-path "/" site-tag "/" site-name))]
    (regex-file-seq #".*\.html" directory)))
;;    (file-seq directory)))

(defn get-site-data
  [base-path rel-path site-tag token]
  "retreive data structure that decribes the site"
  (let [mv-path (str  base-path  "/" rel-path "/" token "/" token ".json")
        data (get (parse-string (slurp mv-path) true) (keyword site-tag))]
    (get  data (first (keys data)))))

(defn populate-contents [base-path rel-path site-tag files token]
  "gets all the content for the single page web app view of the site"  
  (let [f (filter fs/file? files)
        pages (map slurp f)
        headers (map get-header (:page (get-site-data  base-path rel-path site-tag token)))]
        (map (fn [x] {:header (first x) :contents (last x)}) (zipmap headers pages))))

;; INTERFACE SPECIFICATION
(defprotocol CMS-Site
  "Site content file lookup interface"
  (get-site-contents [this token]
    "Gets all the pages and prepares them for the view")
  (get-site-menu [this token]
    "Retrieves the menu configuration for the site"))

;; IMPLEMENTATION CONSTRUCTOR
(defn new-landing-site
  [{:keys[base-path site-cfg-path site-tag] :as settings}]
  (let [one 1]
      (reify CMS-Site
        ;; PROTOCOL METHOD IMPLEMENTATIONS    
        (get-site-contents [this token]
          (let [options {:base-path base-path :rel-path site-cfg-path :site-tag site-tag :token token}
                site-id (get-site-id options)
                page-files (assemble-site-files base-path site-tag site-id)]
          (populate-contents base-path site-cfg-path site-tag page-files token ))))))


(defn new-cms-site
  [{:keys[base-path site-cfg-path site-tag site-name] :as settings}]
  (let [one 1]
      (reify CMS-Site

        ;; PROTOCOL METHOD IMPLEMENTATIONS    
        (get-site-contents [this token]
          (let [options {:base-path base-path :rel-path site-cfg-path
                         :site-tag site-tag :token token}
                page-files (assemble-site-files base-path site-tag site-name)]
            (populate-contents base-path site-cfg-path site-tag page-files token )))

        (get-site-menu [this token]
          (:site_nav_header (first (:single_page_webapp (get-site-data  base-path site-cfg-path site-tag token))))))))
